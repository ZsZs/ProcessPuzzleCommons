package hu.itkodex.commons.persistence.query;

import java.util.Iterator;

public interface AttributeFilter extends Cloneable {
   void addAggregateFunction( AggregateFunction function );
   void addAttributeSelector( AttributeSelector selector );
   Iterator<AttributeFilterExpression> attributesIterator();
   AttributeFilter clone();
}
