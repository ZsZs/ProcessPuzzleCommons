package com.processpuzzle.commons.persistence.query;

import com.processpuzzle.commons.persistence.Entity;
import com.processpuzzle.commons.persistence.ValueObject;

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
