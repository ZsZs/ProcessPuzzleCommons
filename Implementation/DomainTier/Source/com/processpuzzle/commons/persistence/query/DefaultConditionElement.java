package com.processpuzzle.commons.persistence.query;

import com.processpuzzle.commons.persistence.query.ConditionElement;

public abstract class DefaultConditionElement implements ConditionElement {
   public abstract ConditionElementType getType();
}
