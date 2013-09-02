package com.processpuzzle.commons.reflection;

import static java.lang.System.out;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

public class ClassSpy {
   public static void spyMethod( Method method ) {
      String fmt = "%24s: %s%n";

      out.format( "\n\n%s%n", method.toGenericString() );
      out.format( fmt, "ReturnType: ", method.getReturnType() );
      out.format( fmt, "GenericReturnType: ", method.getGenericReturnType() );
      out.format( fmt, "Is abstract: ", Modifier.isAbstract( method.getModifiers() ));
      out.format( fmt, "Is native: ", Modifier.isNative( method.getModifiers() ));
      out.format( fmt, "Is interface: ", Modifier.isInterface( method.getModifiers() ));
      out.format( fmt, "Is transient: ", Modifier.isTransient( method.getModifiers() ));
      out.format( fmt, "Is volatile: ", Modifier.isVolatile( method.getModifiers() ));

      Class<?>[] pType = method.getParameterTypes();
      Type[] gpType = method.getGenericParameterTypes();
      for( int i = 0; i < pType.length; i++ ){
         out.format( fmt, "ParameterType", pType[i] );
         out.format( fmt, "GenericParameterType", gpType[i] );
      }

      Class<?>[] xType = method.getExceptionTypes();
      Type[] gxType = method.getGenericExceptionTypes();
      for( int i = 0; i < xType.length; i++ ){
         out.format( fmt, "ExceptionType", xType[i] );
         out.format( fmt, "GenericExceptionType", gxType[i] );
      }
   }
}
