package hu.itkodex.commons.persistence.query;

import hu.itkodex.commons.persistence.Entity;
import hu.itkodex.commons.persistence.ValueObject;

public interface Query extends Cloneable, ValueObject {
   Query clone();
   AttributeFilter getAttributeFilter();
   Integer getFirstResult();
   Integer getMaxResults();
   Class<? extends Entity> getTargetClass();
   QueryCondition<?> getQueryCondition();
   QueryContext getQueryContext();
   QueryOrder getQueryOrder();
   void setFirstResult( int firstResult );
   void setMaxResults( int maxResults );
}
