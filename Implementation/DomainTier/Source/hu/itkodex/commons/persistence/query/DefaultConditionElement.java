package hu.itkodex.commons.persistence.query;

import hu.itkodex.commons.persistence.query.ConditionElement;

public abstract class DefaultConditionElement implements ConditionElement {
   public abstract ConditionElementType getType();
}
