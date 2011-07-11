package hu.itkodex.commons.persistence.query;

import java.util.Date;

public interface QueryContext extends Cloneable {
   void addDateValueFor( String attributeName, Date value );
   void addIntegerValueFor( String attributeName, Integer value );
   void addTextValueFor( String attributeName, String value );
   QueryContext clone();
   Object getAttributeValue( String attributeName );
}
