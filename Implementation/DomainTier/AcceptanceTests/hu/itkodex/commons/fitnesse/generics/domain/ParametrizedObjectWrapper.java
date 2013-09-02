package hu.itkodex.commons.fitnesse.generics.domain;

import java.util.Date;

import com.processpuzzle.commons.generics.TestParametrizedClass;

public class ParametrizedObjectWrapper {
   private Object parametrizedObject;

   public void instantiateParametrizedObject() {
      parametrizedObject = new TestParametrizedClass<String, Date>();
   }

   public Object getParametrizedObject() {
      return parametrizedObject;
   }
}