package hu.itkodex.commons.persistence.query;

import java.util.Iterator;

public interface QueryOrder extends Cloneable {
   void addOrderSpecifier( OrderSpecifier specifier );
   QueryOrder clone();
   Iterator<OrderSpecifier> specifiersIterator();
}
