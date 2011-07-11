package hu.itkodex.commons.rdbms;

import hu.itkodex.commons.persistence.AggregateRoot;
import hu.itkodex.commons.persistence.PersistenceProvider;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.MessageFormat;

import org.hibernate.mapping.Table;

public class DatabaseSpy {
   private static final String SQL_TEMPLATE = "SELECT * FROM {0} WHERE ID = {1}";
   private static final String SQL_TEMPLATE_WHERE = "SELECT * FROM {0} WHERE {1} = {2}";
   private Connection databaseConnection;
   private Class<? extends AggregateRoot> rootClass;
   private ResultSet rootRecord;
   private Table rootTable;
   private String connectionUrl;
   private String driverClass;
   private String userName;
   private String password;
   
   public DatabaseSpy( PersistenceProvider persistenceProvider ) {
      this.driverClass = persistenceProvider.getDriverClass();
      this.connectionUrl = persistenceProvider.getConnectionUrl();
      this.userName = persistenceProvider.getUserName();
      this.password = persistenceProvider.getPassword();
   }
   
   public void connect() {
      try{
         Class.forName( driverClass );
         databaseConnection = DriverManager.getConnection( connectionUrl, userName, password );
      }catch( SQLException e ){
         e.printStackTrace();
      }catch( ClassNotFoundException e ){
         throw new DatabaseDriverClassNotFound( driverClass, connectionUrl, e );
      }
   }

   @SuppressWarnings( "unchecked" )
   public <D> D retrieveColumnFromRecord( ResultSet resultSet, Class<D> dataType, String columnName ) throws SQLException {

      switch( DataType.getTypeIndex( dataType ) ) {
         case DATE:
            return (D) resultSet.getDate( columnName );
         case TIMESTAMP:
            return (D) resultSet.getTimestamp( columnName );
         case INTEGER:
            return (D) new Integer( resultSet.getInt( columnName ) );
         case DOUBLE:
            if( resultSet.getObject( columnName ) == null ){
               return null;
            }else{
               double value = resultSet.getDouble( columnName );
               return (D) new Double( value );
            }
         case STRING:
            return (D) resultSet.getString( columnName );
         case BOOLEAN:
            return (D) new Boolean( resultSet.getBoolean( columnName ) );
         default:
            return (D) resultSet.getString( columnName );
      }
   }

   public <D> D retrieveColumnFromRootRecord( Class<D> dataType, String columnName ) {
      D columnValue = null;
      try{
         columnValue = retrieveColumnFromRecord( rootRecord, dataType, columnName );
      }catch( SQLException e ){
         e.printStackTrace();
      }
      return columnValue;
   }

   public <D> D retrieveColumnFromRow( String table, Integer id, Class<D> dataType, String columnName ) {
      return retrieveColumnFromRow( table, null, id, dataType, columnName );
   }

   public <D> D retrieveColumnFromRow( String table, String whereFilterColumnName, Integer id, Class<D> dataType, String columnName ) {
      ResultSet resultSet = retrieveRecord( table, whereFilterColumnName, id );
      D columnValue = null;
      try{
         columnValue = retrieveColumnFromRecord( resultSet, dataType, columnName );
      }catch( SQLException e ){
         e.printStackTrace();
      }
      return columnValue;
   }

   public void retrieveRootObjectsRecord( AggregateRoot root ) {
      if( java.lang.reflect.Modifier.isAbstract( rootClass.getModifiers() ) ){
         rootRecord = null;
      }else {
         rootRecord = retrieveRecord( rootTable.getName(), root.getId() );
      }
   }

   public ResultSet retrieveRecord( String tableName, Integer id ) {
      return retrieveRecord( tableName, null, id );
   }

   public ResultSet retrieveRecord( String tableName, String whereFilterColumnName, Integer id ) {
      ResultSet resultSet = null;
      Statement statement;
      try{
         statement = databaseConnection.createStatement();
         String queryStatement = "";
         if( whereFilterColumnName == null ){
            queryStatement = MessageFormat.format( SQL_TEMPLATE, new Object[] { tableName, id.toString() } );
         }else{
            queryStatement = MessageFormat.format( SQL_TEMPLATE_WHERE, new Object[] { tableName, whereFilterColumnName, id.toString() } );

         }
         resultSet = statement.executeQuery( queryStatement );
         resultSet.next();
         if( resultSet.getRow() == 0 )
            throw new NoDataAvailableException();
      }catch( SQLException e ){
         throw new DataRetrieveException( tableName, id, Timestamp.class, "Not relevant for this select", e );
      }

      return resultSet;
   }

   public boolean rowDoesNotExist( String table, Integer id ) {
      boolean result = false;
      try{
         retrieveColumnFromRow( table, id, null, null );
      }catch( NoDataAvailableException e ){
         return true;
      }
      return result;
   }

   public enum DataType {
      INTEGER( Integer.class ), DOUBLE( Double.class ), STRING( String.class ), DATE( Date.class ), TIMESTAMP( Timestamp.class ), BOOLEAN( Boolean.class );

      public static DataType getTypeIndex( Class<?> checkedType ) {

         for( DataType dataType : DataType.values() ){
            if( checkedType.equals( dataType.getJavaType() ) )
               return dataType;
         }

         return null;
      }

      public Class<?> getJavaType() {
         return javaType;
      }

      private Class<?> javaType;

      private DataType( Class<?> javaType ) {
         this.javaType = javaType;
      }
   };
}
