package hu.itkodex.commons.persistence.query;

public enum BooleanOperators {
   AND("and"),
   OR("or"),
   XOR("xor");
   
   BooleanOperators(String symbol) {
      this.symbol = symbol;
   }
   private String symbol = null;

   public String getSymbol() {return symbol;}
}
