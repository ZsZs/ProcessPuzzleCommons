package com.processpuzzle.commons.fitnesse.generics.domain;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.processpuzzle.commons.generics.GenericTypeParameterInvestigator;
import com.processpuzzle.commons.text.TextUtil;

import fit.ColumnFixture;

public class TypeParameterInvestigator extends ColumnFixture {
   public String sourceCode;
   public int parameterIndex;
   private static final String COMPILLATION_DIRECTORY = "C:/Temp/JavaCompiler";
   private static final String SOURCE_DIRECTORY = COMPILLATION_DIRECTORY + "/Source";
   private static final String BINARIES_DIRECTORY = COMPILLATION_DIRECTORY + "/Binaries";
   private static final Logger logger = LoggerFactory.getLogger( TypeParameterInvestigator.class );
   private String parametrizedClassName;
   private String parametrizedClassPackage;
   private File parametrizedClassFile;
   private Class<?> parametrizedClass;
   private String parameterType;

   public String parameterTypeIs() throws FileNotFoundException {
      cleanFolders();
      createFolders();
      determinePackageName();
      determineClassName();
      compileParametrizedClass();
      loadParametrizedClass();
      investigateParameterType();
      return parameterType;
   }

   // Properties
   public String getTestClassSource() { return sourceCode; }
   public Class<?> getParametrizedClass() { return parametrizedClass; }
   public File getParametrizedClassFile() { return parametrizedClassFile; }
   public String getParametrizedClassName() { return parametrizedClassName; }
   public String getParametrizedClassPackage() { return parametrizedClassPackage; }

   // Private helper methods
   private void cleanFolders() {
      deleteDirectory( new File( SOURCE_DIRECTORY ));
      deleteDirectory( new File( BINARIES_DIRECTORY ));
   }
   
   private void createFolders() {
      File sourceDirectory = new File( SOURCE_DIRECTORY );
      sourceDirectory.mkdirs();
      
      File binaryDirectory = new File( BINARIES_DIRECTORY );
      binaryDirectory.mkdirs();
   }
   
   private void compileParametrizedClass() throws FileNotFoundException {
      logger.debug( "Intending to compile class:'" + parametrizedClassPackage + "." + parametrizedClassName + "' source:\n" + sourceCode );
      
      FileOutputStream javaFile;
      String sourceFileName = SOURCE_DIRECTORY + "/" + parametrizedClassName + ".java";
      try{
         javaFile = new FileOutputStream( sourceFileName );
         javaFile.write( sourceCode.getBytes() );
         javaFile.close();
      }catch( FileNotFoundException e ){
         throw new FileNotFoundException("Source file:'" + sourceFileName + "' couldn't be created.");
      }catch( IOException e ){
         throw new FileNotFoundException("Source file:'" + sourceFileName + "' couldn't be created.");
      }
      
      JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
      if( javaCompiler == null ) throw new JavaCompilerNotFoundException();
      
      StandardJavaFileManager fileManager = javaCompiler.getStandardFileManager( null, null, null );
      
      Iterable<? extends JavaFileObject> fileObjects = fileManager.getJavaFileObjects( new File( sourceFileName ));
      String[] options = new String[] { "-d", BINARIES_DIRECTORY };
      DiagnosticListener<JavaFileObject> diagnosticListener = new ExampleDiagnosticListener();
      CompilationTask task = javaCompiler.getTask( null, fileManager, diagnosticListener, Arrays.asList( options ), null, fileObjects );
      task.call();

      try{
         fileManager.close();
      }catch( IOException e ){
         e.printStackTrace();
      }
      
      parametrizedClassFile = new File( BINARIES_DIRECTORY + "/" + parametrizedClassPackage.replace( ".", "/" ) + "/" + parametrizedClassName + ".class" );
   }

   private boolean deleteDirectory( File path ) {
      if( path.exists() ) {
        File[] files = path.listFiles();
        for(int i=0; i<files.length; i++) {
           if(files[i].isDirectory()) {
             deleteDirectory(files[i]);
           }
           else {
             files[i].delete();
           }
        }
      }
      return( path.delete() );
   }
   
   private void determineClassName() {
      parametrizedClassName = TextUtil.extractText( sourceCode, " class ", " " ); 
   }
   
   private void determinePackageName() {
      parametrizedClassPackage = TextUtil.extractText( sourceCode, " package ", ";" );
   }

   private void investigateParameterType() {
      logger.trace( "GenericTypeInvestigator.getTypeParameter(" + parametrizedClass.getName() + ", " + parameterIndex + ") should be called.");
      Class<?> parameterTypeClass = GenericTypeParameterInvestigator.getTypeParameter( parametrizedClass, parameterIndex );
      
      if( parameterTypeClass != null ) parameterType = parameterTypeClass.getName();
   }
   
   private void loadParametrizedClass() {
      URL[] classFileUrls;
      try{
         URL binaryUrl = new URL( "file://" + BINARIES_DIRECTORY + "/" );
         classFileUrls = new URL[] { binaryUrl };
         URLClassLoader urlClassLoader = new URLClassLoader( classFileUrls );
         parametrizedClass = urlClassLoader.loadClass( parametrizedClassPackage + "." + parametrizedClassName );
      }catch( MalformedURLException e ){
         e.printStackTrace();
      }catch( ClassNotFoundException e ){
         e.printStackTrace();
      }
      
      logger.trace( "Parametrized class: '" + parametrizedClass.getName() + "' was loaded with class loader: '" + parametrizedClass.getClassLoader() +"'" );
   }
   
   private static class ExampleDiagnosticListener implements DiagnosticListener<JavaFileObject> {

      @Override public void report( Diagnostic<? extends JavaFileObject> diagnostic ) {
         StringBuffer messageString = new StringBuffer();
         messageString.append( "Code: " ).append( diagnostic.getCode() ).append( '\n' );
         messageString.append( "Kind: " ).append( diagnostic.getKind() ).append( '\n' );
         messageString.append( "Position: " ).append( diagnostic.getPosition() ).append( '\n' );
         messageString.append( "Start Position: " ).append( diagnostic.getStartPosition() ).append( '\n' );
         messageString.append( "End Position: " ).append( diagnostic.getEndPosition() ).append( '\n' );
         messageString.append( "Source: " ).append( diagnostic.getSource() );

         System.out.println( messageString );
      }
   }
}
