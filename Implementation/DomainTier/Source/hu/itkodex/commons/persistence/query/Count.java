package hu.itkodex.commons.persistence.query;

public class Count extends AggregateFunction {

   public Count( String attributeName ) {
      super( attributeName );
      functionNames.put( QueryTransformer.SupportedTrasformers.HQL, "count" );
   }
}
