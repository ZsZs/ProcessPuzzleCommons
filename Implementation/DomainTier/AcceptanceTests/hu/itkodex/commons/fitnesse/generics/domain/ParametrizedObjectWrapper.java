package hu.itkodex.commons.fitnesse.generics.domain;

import hu.itkodex.commons.generics.TestParametrizedClass;
import java.util.Date;

public class ParametrizedObjectWrapper {
   private Object parametrizedObject;

   public void instantiateParametrizedObject() {
      parametrizedObject = new TestParametrizedClass<String, Date>();
   }

   public Object getParametrizedObject() {
      return parametrizedObject;
   }
}