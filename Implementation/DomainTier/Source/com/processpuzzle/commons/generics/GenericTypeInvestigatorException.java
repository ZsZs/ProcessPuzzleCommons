package com.processpuzzle.commons.generics;

import java.text.MessageFormat;

public class GenericTypeInvestigatorException extends RuntimeException {
   private static final long serialVersionUID = -4508724352010077132L;
   private static String defaultMessagePattern = "Unexpected exception was catched investigating generic type: '''{0}'''.";
   private Class<?> type;
   
   public GenericTypeInvestigatorException( Class<?> type, Throwable cause ) {
      super( MessageFormat.format( defaultMessagePattern, new Object[] { type.getName() } ));
      this.type = type;
   }

   public Class<?> getType() { return type; }
}
