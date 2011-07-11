/*
 * MemClassLoader.java
 *
 * Created on 27 September 2007, 11:53
 *
 * based on code posted at 
 * http://www.velocityreviews.com/forums/t318697-javacompilertool.html
 *
 */

package hu.itkodex.commons.compiler.hickory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;
import javax.tools.JavaFileObject.Kind;

/**
 * A class loader that loads what's in the cache by preference, and if it can't find the class there, loads from the
 * standard parent.
 */
final class MemClassLoader extends ClassLoader {
   final Map<LocationAndKind, Map<String, JavaFileObject>> ramFileSystem;
   final private static LocationAndKind CLASS_KEY = new LocationAndKind( StandardLocation.CLASS_OUTPUT, Kind.CLASS );
   final private static LocationAndKind SOURCE_KEY = new LocationAndKind( StandardLocation.CLASS_OUTPUT, Kind.SOURCE );
   final private static LocationAndKind OTHER_KEY = new LocationAndKind( StandardLocation.CLASS_OUTPUT, Kind.OTHER );

   // TODO need to access file manager for getresource type methods
   // could use FileMagr .list maybe, may need to adjust
   // name if .class file, convert path to fqn
   MemClassLoader( Map<LocationAndKind, Map<String, JavaFileObject>> ramFileSystem ) {
      this.ramFileSystem = ramFileSystem;
   }

   @Override
   public InputStream getResourceAsStream( String name ) {
      JavaFileObject javaFileObject = getFileObject( name );
      if( javaFileObject != null ){
         byte[] bytes = ((MemJavaFileObject) javaFileObject).baos.toByteArray();
         return new ByteArrayInputStream( bytes );
      }
      return null;
   }

   @Override
   public Enumeration<URL> getResources( String name ) throws IOException {
      List<URL> retValue;
      retValue = Collections.list( super.getResources( name ) );
      JavaFileObject javaFileObject = getFileObject( name );

      if( javaFileObject != null )
         retValue.add( javaFileObject.toUri().toURL() );
      return Collections.enumeration( retValue );
   }

   @Override
   protected Class<?> findClass( String name ) throws ClassNotFoundException {
      JavaFileObject javaFileObject = ramFileSystem.get( CLASS_KEY ).get( name );
      if( javaFileObject != null ){
         byte[] bytes = ((MemJavaFileObject) javaFileObject).baos.toByteArray();
         return defineClass( name, bytes, 0, bytes.length );
      }
      return super.findClass( name );
   }

   @Override
   protected URL findResource( String name ) {
      URL retValue;

      retValue = super.findResource( name );
      if( retValue != null ){
         return retValue;
      }else{
         JavaFileObject jfo = getFileObject( name );
         if( jfo != null ){
            try{
               return jfo.toUri().toURL();
            }catch( MalformedURLException ex ){
               return null;
            }
         }else{
            return null;
         }
      }
   }

   private JavaFileObject getFileObject( String name ) {
      LocationAndKind key = OTHER_KEY;
      if( name.endsWith( Kind.CLASS.extension ) ){
         name = name.replace( ".", "/" ) + Kind.CLASS.extension;
         key = CLASS_KEY;
      }else if( name.endsWith( Kind.SOURCE.extension ) ){
         name = name.replace( ".", "/" ) + Kind.SOURCE.extension;
         key = SOURCE_KEY;
      }

      if( !ramFileSystem.containsKey( key ) ){
         return null;
      }

      return ramFileSystem.get( key ).get( name );
   }
}
