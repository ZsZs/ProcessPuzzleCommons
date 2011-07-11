package hu.itkodex.commons.persistence.query;

public abstract class Operator extends DefaultConditionElement{
   protected Object operatorConstant = null;
   
   public abstract String toString();
   
//Properties
   @Override
   public ConditionElementType getType() { return null; }
//   public String getOperator() {return operator.getSymbol(); 
}
