package hu.itkodex.commons.persistence;

public interface PersistenceProvider {
   public String getDriverClass();
   public String getConnectionUrl();
   public String getUserName();
   public String getPassword();

}
