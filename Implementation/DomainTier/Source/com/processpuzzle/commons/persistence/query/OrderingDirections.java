package com.processpuzzle.commons.persistence.query;

public enum OrderingDirections {
   Ascending("asc", "ascending"),
   Descending("desc", "descending");

   String HQLRepresentation = null;
   String SQLRepresentation = null;
   
   OrderingDirections( String HQLForm, String SQLForm ) {
      this.HQLRepresentation = HQLForm;
      this.SQLRepresentation = SQLForm;
   }
   
   public String getAsHQL() { return HQLRepresentation; }
   public String getAsSQL() { return SQLRepresentation; }
}
