package hu.itkodex.commons.rdbms;

import com.ibm.icu.text.MessageFormat;

public class DatabaseConnectionException extends RuntimeException {
   private static final long serialVersionUID = 4901535322049397897L;
   private static final String message = "Connecting to database ''{0}'' failed.";
   private String connectionString;
   
   public DatabaseConnectionException( String connectionString, Throwable cause ) {
      super( MessageFormat.format( message, new Object[] { connectionString } ), cause );
      this.connectionString = connectionString;
   }
   
   public String getConnectionString() { return connectionString; }
}
