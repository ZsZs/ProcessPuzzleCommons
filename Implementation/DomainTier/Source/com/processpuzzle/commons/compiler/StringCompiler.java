package com.processpuzzle.commons.compiler;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.DiagnosticListener;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject.Kind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.processpuzzle.commons.text.TextUtil;

/**
 * Compile a String or other {@link CharSequence}, returning a Java {@link Class} instance that may be instantiated.
 * This class is a Facade around {@link JavaCompiler} for a narrower use case, but a bit easier to use.
 * <p>
 * To compile a String containing source for a Java class which implements MyInterface:
 * 
 * <pre>
 * ClassLoader classLoader = MyClass.class.getClassLoader(); // optional; null is also OK 
 * List&lt;Diagnostic&gt; diagnostics = new ArrayList&lt;Diagnostic&gt;(); // optional; null is also OK
 * JavaStringCompiler&lt;Object&gt; compiler = new JavaStringCompiler&lt;MyInterface&gt;( classLoader, null );
 * try{
 *    Class&lt;MyInterface&gt; newClass = compiler.compile( &quot;com.mypackage.NewClass&quot;, stringContaininSourceForNewClass, diagnostics, MyInterface );
 *    MyInterface instance = newClass.newInstance();
 *    instance.someOperation( someArgs );
 * }catch( JavaStringCompilerException e ){
 *    handle( e );
 * }catch( IllegalAccessException e ){
 *    handle( e );
 * }
 * </pre>
 * 
 * The source can be in a String, {@link StringBuffer}, or your own class which implements {@link CharSequence}. If you
 * implement your own, it must be thread safe (preferably, immutable.)
 * 
 * @author <a href="mailto:David.Biesack@sas.com">David J. Biesack</a>
 */
public class StringCompiler<T> {
   static final String JAVA_EXTENSION = ".java";    // Compiler requires source files with a ".java" extension
   private static final Logger logger = LoggerFactory.getLogger( StringCompiler.class );
   private final InMemoryClassLoader classLoader;
   private final JavaCompiler compiler;             // The compiler instance that this facade uses.
   private final List<String> options;              // The compiler options (such as "-target" "1.5").
   private DiagnosticListener<JavaFileObject> diagnostics;   // collect compiler diagnostics in this instance.
   private final FileManagerImpl javaFileManager;   // The FileManager which will store source and class "files".
   private boolean isSuccess = false;

   public StringCompiler( ClassLoader loader, Iterable<String> options ) {
      this( loader, options, null );
   }
      
   /**
    * Construct a new instance which delegates to the named class loader.
    * 
    * @param loader
    *           the application ClassLoader. The compiler will look through to this // class loader for dependent
    *           classes
    * @param options
    *           The compiler options (such as "-target" "1.5"). See the usage for javac
    * @throws IllegalStateException
    *            if the Java compiler cannot be loaded.
    */
   public StringCompiler( ClassLoader loader, Iterable<String> options, StringCompiler<?> previousCompiler ) {
      compiler = ToolProvider.getSystemJavaCompiler();

      if( compiler == null ){
         throw new IllegalStateException( "Cannot find the system Java compiler. " + "Check that your class path includes tools.jar" );
      }
      
      diagnostics = new StringCompilerDiagnosticListener();
      
      if( previousCompiler != null && !previousCompiler.isSuccess )
         throw new IllegalStateException( "Previous compilation has not yet been compiled." );
      
      if( previousCompiler != null ) {
         javaFileManager = previousCompiler.getFileManager();
         classLoader = javaFileManager.getClassLoader();
      }
      else {
         classLoader = new InMemoryClassLoader( loader );
         JavaFileManager fileManager = compiler.getStandardFileManager( diagnostics, null, null );
         
         // create our FileManager which chains to the default file manager and our ClassLoader
         javaFileManager = new FileManagerImpl( fileManager, classLoader );
      }
      
      this.options = new ArrayList<String>();
      if( options != null ){ // make a save copy of input options
         for( String option : options ){
            this.options.add( option );
         }
      }
   }

   /**
    * Compile Java source in <var>javaSource</name> and return the resulting class.
    * <p>
    * Thread safety: this method is thread safe if the <var>javaSource</var> and <var>diagnosticsList</var> are isolated
    * to this thread.
    * 
    * @param javaSource
    *           Complete java source, including a package statement and a class, interface, or annotation declaration.
    * @param diagnosticsList
    *           Any diagnostics generated by compiling the source are added to this collector.
    * @param types
    *           zero or more Class objects representing classes or interfaces that the resulting class must be
    *           assignable (castable) to.
    * @return a Class which is generated by compiling the source
    * @throws CharSequenceCompilerException
    *            if the source cannot be compiled - for example, if it contains syntax or semantic errors or if
    *            dependent classes cannot be found.
    * @throws ClassCastException
    *            if the generated class is not assignable to all the optional <var>types</var>.
    */
   public synchronized Class<T> compile( 
         final CharSequence javaSource,
         final DiagnosticCollector<JavaFileObject> diagnosticsList, 
         final Class<?>... types ) throws StringCompilerException, ClassCastException {
      String qualifiedClassName = determineQualifiedClassName( javaSource ); 
      return compile( qualifiedClassName, javaSource, diagnosticsList, types );
   }
   /**
    * Compile Java source in <var>javaSource</name> and return the resulting class.
    * <p>
    * Thread safety: this method is thread safe if the <var>javaSource</var> and <var>diagnosticsList</var> are isolated
    * to this thread.
    * 
    * @param qualifiedClassName
    *           The fully qualified class name.
    * @param javaSource
    *           Complete java source, including a package statement and a class, interface, or annotation declaration.
    * @param diagnosticsList
    *           Any diagnostics generated by compiling the source are added to this collector.
    * @param types
    *           zero or more Class objects representing classes or interfaces that the resulting class must be
    *           assignable (castable) to.
    * @return a Class which is generated by compiling the source
    * @throws CharSequenceCompilerException
    *            if the source cannot be compiled - for example, if it contains syntax or semantic errors or if
    *            dependent classes cannot be found.
    * @throws ClassCastException
    *            if the generated class is not assignable to all the optional <var>types</var>.
    */
   public synchronized Class<T> compile( 
         final String qualifiedClassName, 
         final CharSequence javaSource,
         final DiagnosticCollector<JavaFileObject> diagnosticsList, 
         final Class<?>... types ) throws StringCompilerException, ClassCastException {
      if( diagnosticsList != null )
         diagnostics = diagnosticsList;
      else
         diagnostics = new StringCompilerDiagnosticListener();
      Map<String, CharSequence> classes = new HashMap<String, CharSequence>( 1 );
      classes.put( qualifiedClassName, javaSource );
      Map<String, Class<T>> compiled = compile( classes, diagnosticsList );
      Class<T> newClass = compiled.get( qualifiedClassName );
      return castable( newClass, types );
   }

   /**
    * Compile multiple Java source strings and return a Map containing the resulting classes.
    * <p>
    * Thread safety: this method is thread safe if the <var>classes</var> and <var>diagnosticsList</var> are isolated to
    * this thread.
    * 
    * @param classes
    *           A Map whose keys are qualified class names and whose values are the Java source strings containing the
    *           definition of the class. A map value may be null, indicating that compiled class is expected, although
    *           no source exists for it (it may be a non-public class contained in one of the other strings.)
    * @param diagnosticsList
    *           Any diagnostics generated by compiling the source are added to this list.
    * @return A mapping of qualified class names to their corresponding classes. The map has the same keys as the input
    *         <var>classes</var>; the values are the corresponding Class objects.
    * @throws CharSequenceCompilerException
    *            if the source cannot be compiled
    */
   public synchronized Map<String, Class<T>> compile( 
         final Map<String, CharSequence> classes,
         final DiagnosticCollector<JavaFileObject> diagnosticsList ) throws StringCompilerException {
      List<JavaFileObject> sources = new ArrayList<JavaFileObject>();
      
      for( Entry<String, CharSequence> entry : classes.entrySet() ){
         String qualifiedClassName = entry.getKey();
         CharSequence javaSource = entry.getValue();
         if( javaSource != null ){
            final int dotPos = qualifiedClassName.lastIndexOf( '.' );
            final String className = dotPos == -1 ? qualifiedClassName : qualifiedClassName.substring( dotPos + 1 );
            final String packageName = dotPos == -1 ? "" : qualifiedClassName.substring( 0, dotPos );
            final JavaFileObjectImpl source = new JavaFileObjectImpl( className, javaSource );
            sources.add( source );
            // Store the source file in the FileManager via package/class name.
            // For source files, we add a .java extension
            javaFileManager.putFileForInput( StandardLocation.SOURCE_PATH, packageName, className + JAVA_EXTENSION, source );
         }
      }
      
      // Get a CompliationTask from the compiler and compile the sources
      final CompilationTask task = compiler.getTask( null, javaFileManager, diagnostics, options, null, sources );
      final Boolean result = task.call();
      
      if( result == null || !result.booleanValue() ){
         logger.error( "Compilation failed:" + buildDiagnosticsMessage( diagnostics ));
         throw new StringCompilerException( "Compilation failed.", classes.keySet(), diagnostics );
      }
      
      try{
         // For each class name in the inpput map, get its compiled class and put it in the output map
         Map<String, Class<T>> compiled = new HashMap<String, Class<T>>();
         for( String qualifiedClassName : classes.keySet() ){
            final Class<T> newClass = loadClass( qualifiedClassName );
            compiled.put( qualifiedClassName, newClass );
         }
         isSuccess = true;
         return compiled;
      }catch( ClassNotFoundException e ){
         throw new StringCompilerException( classes.keySet(), e, diagnostics );
      }catch( IllegalArgumentException e ){
         throw new StringCompilerException( classes.keySet(), e, diagnostics );
      }catch( SecurityException e ){
         throw new StringCompilerException( classes.keySet(), e, diagnostics );
      }
   }

   /**
    * Load a class that was generated by this instance or accessible from its parent class loader. Use this method if
    * you need access to additional classes compiled by
    * {@link #compile(String, CharSequence, DiagnosticCollector, Class...) compile()}, for example if the primary class
    * contained nested classes or additional non-public classes.
    * 
    * @param qualifiedClassName
    *           the name of the compiled class you wish to load
    * @return a Class instance named by <var>qualifiedClassName</var>
    * @throws ClassNotFoundException
    *            if no such class is found.
    */
   @SuppressWarnings( "unchecked" )
   public Class<T> loadClass( final String qualifiedClassName ) throws ClassNotFoundException {
      return (Class<T>) classLoader.loadClass( qualifiedClassName );
   }

   /**
    * @return This compiler's class loader.
    */
   public ClassLoader getClassLoader() { return javaFileManager.getClassLoader(); }
   
   public FileManagerImpl getFileManager() { return javaFileManager; }
   
   public boolean isSuccess() { return isSuccess; }

   /**
    * COnverts a String to a URI.
    * 
    * @param name
    *           a file name
    * @return a URI
    */
   static URI toURI( String name ) {
      try{
         return new URI( name );
      }catch( URISyntaxException e ){
         logger.error( "String: '" + name + "' can't be converted to URI. There is a syntactical problem.", e );
         throw new RuntimeException( e );
      }
   }

   private String buildDiagnosticsMessage( DiagnosticListener<JavaFileObject> diagnostics ) {
      String message = "";
      return message;
   }

   /**
    * Check that the <var>newClass</var> is a subtype of all the type parameters and throw a ClassCastException if not.
    * 
    * @param types
    *           zero of more classes or interfaces that the <var>newClass</var> must be castable to.
    * @return <var>newClass</var> if it is castable to all the types
    * @throws ClassCastException
    *            if <var>newClass</var> is not castable to all the types.
    */
   private Class<T> castable( Class<T> newClass, Class<?>... types ) throws ClassCastException {
      for( Class<?> type : types )
         if( !type.isAssignableFrom( newClass ) ){
            logger.error( "New class:'" + newClass.getName() + "' can't be casted to:'" + type.getName() + "'");
            throw new ClassCastException( type.getName() );
         }
      return newClass;
   }

   
   private String determineQualifiedClassName( CharSequence javaSource ) {
      String className = TextUtil.extractText( javaSource.toString(), " class ", " " );
      if( className.indexOf( "<" ) >= 0 ) className = className.substring( 0, className.indexOf( "<" ));
      
      String packageName = TextUtil.extractText( javaSource.toString(), " package ", ";" );
      return packageName + "." + className;
   }

   private static final class StringCompilerDiagnosticListener implements DiagnosticListener<JavaFileObject> {

      @Override public void report( Diagnostic<? extends JavaFileObject> diagnostic ) {
         StringBuffer messageString = new StringBuffer();
         messageString.append( "\nCode: " ).append( diagnostic.getCode() ).append( '\n' );
         messageString.append( "Message: " ).append( diagnostic.getMessage( null ) ).append( "" );
         messageString.append( " at line: " ).append( diagnostic.getLineNumber() ).append( "" );
         messageString.append( " column: " ).append( diagnostic.getColumnNumber() ).append( '\n' );
         messageString.append( "Source file: " ).append( diagnostic.getSource() );

         logger.debug( messageString.toString() );
         System.out.println( messageString );
      }
   }
}

/**
 * A JavaFileManager which manages Java source and classes. This FileManager delegates to the JavaFileManager and the
 * ClassLoaderImpl provided in the constructor. The sources are all in memory CharSequence instances and the classes are
 * all in memory byte arrays.
 */
final class FileManagerImpl extends ForwardingJavaFileManager<JavaFileManager> {
   private final InMemoryClassLoader classLoader;      // the delegating class loader (passed to the constructor)
   private final Map<URI, JavaFileObject> fileObjects = new HashMap<URI, JavaFileObject>();    // Internal map of filename URIs to JavaFileObjects.

   /**
    * Construct a new FileManager which forwards to the <var>fileManager</var> for source and to the
    * <var>classLoader</var> for classes
    * 
    * @param fileManager
    *           another FileManager that this instance delegates to for additional source.
    * @param classLoader
    *           a ClassLoader which contains dependent classes that the compiled classes will require when compiling
    *           them.
    */
   public FileManagerImpl( JavaFileManager fileManager, InMemoryClassLoader classLoader ) {
      super( fileManager );
      this.classLoader = classLoader;
   }

   /**
    * @return the class loader which this file manager delegates to
    */
   public InMemoryClassLoader getClassLoader() {
      return classLoader;
   }

   /**
    * For a given file <var>location</var>, return a FileObject from which the compiler can obtain source or byte code.
    * 
    * @param location
    *           an abstract file location
    * @param packageName
    *           the package name for the file
    * @param relativeName
    *           the file's relative name
    * @return a FileObject from this or the delegated FileManager
    * @see javax.tools.ForwardingJavaFileManager#getFileForInput(javax.tools.JavaFileManager.Location, java.lang.String,
    *      java.lang.String)
    */
   @Override
   public FileObject getFileForInput( Location location, String packageName, String relativeName ) throws IOException {
      FileObject fileObject = fileObjects.get( uri( location, packageName, relativeName ) );
      if( fileObject != null )
         return fileObject;
      return super.getFileForInput( location, packageName, relativeName );
   }

   /**
    * Store a file that may be retrieved later with
    * {@link #getFileForInput(javax.tools.JavaFileManager.Location, String, String)}
    * 
    * @param location
    *           the file location
    * @param packageName
    *           the Java class' package name
    * @param relativeName
    *           the relative name
    * @param file
    *           the file object to store for later retrieval
    */
   public void putFileForInput( StandardLocation location, String packageName, String relativeName, JavaFileObject file ) {
      fileObjects.put( uri( location, packageName, relativeName ), file );
   }

   /**
    * Create a JavaFileImpl for an output class file and store it in the classloader.
    * 
    * @see javax.tools.ForwardingJavaFileManager#getJavaFileForOutput(javax.tools.JavaFileManager.Location,
    *      java.lang.String, javax.tools.JavaFileObject.Kind, javax.tools.FileObject)
    */
   @Override
   public JavaFileObject getJavaFileForOutput( Location location, String qualifiedName, Kind kind, FileObject outputFile )
         throws IOException {
      JavaFileObject file = new JavaFileObjectImpl( qualifiedName, kind );
      classLoader.add( qualifiedName, file );
      return file;
   }

   @Override
   public ClassLoader getClassLoader( JavaFileManager.Location location ) {
      return classLoader;
   }

   @Override
   public String inferBinaryName( Location loc, JavaFileObject file ) {
      String result;
      // For our JavaFileImpl instances, return the file's name, else
      // simply run the default implementation
      if( file instanceof JavaFileObjectImpl )
         result = file.getName();
      else
         result = super.inferBinaryName( loc, file );
      return result;
   }

   @Override
   public Iterable<JavaFileObject> list( Location location, String packageName, Set<Kind> kinds, boolean recurse ) throws IOException {
      Iterable<JavaFileObject> result = super.list( location, packageName, kinds, recurse );
      ArrayList<JavaFileObject> files = new ArrayList<JavaFileObject>();
      
      if( location == StandardLocation.CLASS_PATH && kinds.contains( JavaFileObject.Kind.CLASS ) ){
         for( JavaFileObject file : fileObjects.values() ){
            if( file.getKind() == Kind.CLASS && file.getName().startsWith( packageName ) )
               files.add( file );
         }
         files.addAll( classLoader.files() );
      }else if( location == StandardLocation.SOURCE_PATH && kinds.contains( JavaFileObject.Kind.SOURCE ) ){
         for( JavaFileObject file : fileObjects.values() ){
            if( file.getKind() == Kind.SOURCE && file.getName().startsWith( packageName ) )
               files.add( file );
         }
      }
      
      for( JavaFileObject file : result ){
         files.add( file );
      }
      return files;
   }
   
   /**
    * Convert a location and class name to a URI
    */
   private URI uri( Location location, String packageName, String relativeName ) {
      return StringCompiler.toURI( location.getName() + '/' + packageName + '/' + relativeName );
   }
}

/**
 * A JavaFileObject which contains either the source text or the compiler generated class. This class is used in two
 * cases.
 * <ol>
 * <li>This instance uses it to store the source which is passed to the compiler. This uses the
 * {@link JavaFileObjectImpl#JavaFileObjectImpl(String, CharSequence)} constructor.
 * <li>The Java compiler also creates instances (indirectly through the FileManagerImplFileManager) when it wants to
 * create a JavaFileObject for the .class output. This uses the
 * {@link JavaFileObjectImpl#JavaFileObjectImpl(String, JavaFileObject.Kind)} constructor.
 * </ol>
 * This class does not attempt to reuse instances (there does not seem to be a need, as it would require adding a Map
 * for the purpose, and this would also prevent garbage collection of class byte code.)
 */
final class JavaFileObjectImpl extends SimpleJavaFileObject {
   private ByteArrayOutputStream byteCode;      // If kind == CLASS, this stores byte code from openOutputStream
   private final CharSequence source;           // If kind == SOURCE, this contains the source text

   JavaFileObjectImpl( final String baseName, final CharSequence source ) {
      super( StringCompiler.toURI( baseName + StringCompiler.JAVA_EXTENSION ), Kind.SOURCE );
      this.source = source;
   }

   JavaFileObjectImpl( final String name, final Kind kind ) {
      super( StringCompiler.toURI( name ), kind );
      source = null;
   }

   @Override public CharSequence getCharContent( final boolean ignoreEncodingErrors ) throws UnsupportedOperationException {
      if( source == null )
         throw new UnsupportedOperationException( "getCharContent()" );
      return source;
   }

   /**
    * Return an input stream for reading the byte code
    * 
    * @see javax.tools.SimpleJavaFileObject#openInputStream()
    */
   @Override
   public InputStream openInputStream() {
      return new ByteArrayInputStream( getByteCode() );
   }

   /**
    * Return an output stream for writing the bytecode
    * 
    * @see javax.tools.SimpleJavaFileObject#openOutputStream()
    */
   @Override
   public OutputStream openOutputStream() {
      byteCode = new ByteArrayOutputStream();
      return byteCode;
   }

   /**
    * @return the byte code generated by the compiler
    */
   byte[] getByteCode() {
      return byteCode.toByteArray();
   }
}