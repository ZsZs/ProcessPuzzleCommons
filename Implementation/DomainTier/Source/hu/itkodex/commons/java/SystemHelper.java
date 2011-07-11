package hu.itkodex.commons.java;

import java.util.Enumeration;
import java.util.Properties;

public class SystemHelper {

   public static void printProperties() {
      Properties property = System.getProperties();
      Enumeration<Object> keys = property.keys();
      while( keys.hasMoreElements() ){
         String key = (String) keys.nextElement();
         String value = (String) property.get( key );
         System.out.println( key + ": " + value );
      }
   }
}
