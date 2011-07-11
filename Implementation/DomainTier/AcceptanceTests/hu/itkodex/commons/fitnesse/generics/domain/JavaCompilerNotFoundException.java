package hu.itkodex.commons.fitnesse.generics.domain;

import java.text.MessageFormat;

public class JavaCompilerNotFoundException extends RuntimeException {
   private static final long serialVersionUID = 4169932059177458202L;
   private static final String message = "Java compiler not found. Please check if JDK is installed an it's on the path. Your JAVA_HOME={0}";
   
   public JavaCompilerNotFoundException() {
      super( MessageFormat.format( message, new Object[] {System.getProperty( "java.home" )} ));
   }
}
