package hu.itkodex.commons.rdbms;

import com.ibm.icu.text.MessageFormat;

public class DatabaseDriverClassNotFound extends RuntimeException {
   private static final long serialVersionUID = 8657398611937692786L;
   private static final String message = "Driver class ''{0}'' not found when trying connect to: ''{1}'' database.";
   private String connectionString;
   private String driverClass;
   
   public DatabaseDriverClassNotFound( String driverClass, String connectionString, Throwable cause ) {
      super( MessageFormat.format( message, new Object[] { driverClass, connectionString } ), cause );
      this.driverClass = driverClass;
      this.connectionString = connectionString;
   }
   
   public String getConnectionString() { return connectionString; }
   public String getDriverClass() { return driverClass; }
}
