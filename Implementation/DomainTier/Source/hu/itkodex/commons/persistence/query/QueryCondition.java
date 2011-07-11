package hu.itkodex.commons.persistence.query;


import java.util.Iterator;
import java.util.Set;

public interface QueryCondition<E extends ConditionElement> extends Cloneable {
   void addAttributeCondition( AttributeCondition condition );
   void addBooleanOperator( BooleanOperator operator );
   int elementsCount();
   Iterator<E> elementsIterator();
   Set<E> getElements();
}
