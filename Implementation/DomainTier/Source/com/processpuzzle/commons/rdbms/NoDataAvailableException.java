package com.processpuzzle.commons.rdbms;

public class NoDataAvailableException extends RuntimeException {
   private static final long serialVersionUID = -1988363025087873239L;

   NoDataAvailableException( Throwable cause ) {
      super( cause );
   }

   public NoDataAvailableException() {
      // TODO Auto-generated constructor stub
   }
}
