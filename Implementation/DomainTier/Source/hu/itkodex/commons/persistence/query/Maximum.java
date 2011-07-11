package hu.itkodex.commons.persistence.query;

public class Maximum extends AggregateFunction {

   public Maximum(String attributeName) {
      super(attributeName);
      functionNames.put(QueryTransformer.SupportedTrasformers.HQL, "max");
   }
}
