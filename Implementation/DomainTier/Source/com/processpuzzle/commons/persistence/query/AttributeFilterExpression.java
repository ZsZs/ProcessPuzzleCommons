package com.processpuzzle.commons.persistence.query;

public abstract class AttributeFilterExpression {
   private String attributeName = null;
   
//Constructors
   AttributeFilterExpression( String attributeName ) {
      this.attributeName = attributeName;
   }
   
   AttributeFilterExpression() {
      //Required by JAXB
   }
 
//Properties
   public String getAttributeName() { return attributeName; }
}
