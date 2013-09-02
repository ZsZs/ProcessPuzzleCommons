package com.processpuzzle.commons.persistence.query;

import java.util.HashMap;
import java.util.Map;

public abstract class AggregateFunction extends AttributeFilterExpression {
   protected Map<QueryTransformer.SupportedTrasformers, String> functionNames = new HashMap<QueryTransformer.SupportedTrasformers, String>();

   AggregateFunction( String attributeName ) {
      super( attributeName );
   }

   public String getNameFor( QueryTransformer.SupportedTrasformers transformer ) {
      return functionNames.get( transformer );
   }
}
