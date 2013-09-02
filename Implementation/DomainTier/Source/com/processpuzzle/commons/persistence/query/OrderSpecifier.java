package com.processpuzzle.commons.persistence.query;

public class OrderSpecifier {
   private String attributeName = null;
   private OrderingDirections direction = null;
   
   public OrderSpecifier( String attributeName, OrderingDirections direction ) {
      this.attributeName = attributeName;
      this.direction = direction;
   }
   
   OrderSpecifier() {
      //Required by JAXB.
   }

//Properties
   public String getAttributeName() { return attributeName; }
   public OrderingDirections getDirection() { return direction; }
}
