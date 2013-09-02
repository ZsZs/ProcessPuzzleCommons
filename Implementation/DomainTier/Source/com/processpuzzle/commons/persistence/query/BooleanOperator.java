package com.processpuzzle.commons.persistence.query;

public class BooleanOperator extends Operator {
   public BooleanOperator( BooleanOperators operator ){
      this.operatorConstant = operator;
   }

   @Override
   public String toString() {
      return ((BooleanOperators) operatorConstant).getSymbol();
   }
}
