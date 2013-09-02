package com.processpuzzle.commons.generics;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;


import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.processpuzzle.commons.generics.GenericTypeInvestigatorException;
import com.processpuzzle.commons.generics.GenericTypeParameterInvestigator;

public class GenericTypeInvestigatorTest {
   @SuppressWarnings("unchecked")
   @Test public void getTypeParameterOf_InvestigatesSubclassParameter() {
      assertThat( (Class<String>) GenericTypeParameterInvestigator.getTypeParameter( DirectSubclassOfParametrizedClass.class, 0 ), equalTo( String.class ));
      assertThat( (Class<Date>) GenericTypeParameterInvestigator.getTypeParameter( DirectSubclassOfParametrizedClass.class, 1 ), equalTo( Date.class ));
   }
   
   @SuppressWarnings("unchecked")
   @Test public void getTypeParameterOf_InvestigatesParentsParameter() {
      assertThat( (Class<String>) GenericTypeParameterInvestigator.getTypeParameter( IndirectSubclassOfParametrizedClass.class, 0 ), equalTo( String.class ));
      assertThat( (Class<Date>) GenericTypeParameterInvestigator.getTypeParameter( IndirectSubclassOfParametrizedClass.class, 1 ), equalTo( Date.class ));
   }
   
   @SuppressWarnings("unchecked")
   @Test public void getTypeParameterOf_InvestigatesAnonymousSubclassParameter() {
      Object parametrizedObject = new TestParametrizedClass<String, Date>() {};
      Class<?> parametrizedClass = parametrizedObject.getClass();
      assertThat( (Class<String>) GenericTypeParameterInvestigator.getTypeParameter( parametrizedClass, 0 ), equalTo( String.class ));
      assertThat( (Class<Date>) GenericTypeParameterInvestigator.getTypeParameter( parametrizedClass, 1 ), equalTo( Date.class ));
   }

   @Test public void getTypeParameterOf_ReturnsNullWhenParameterNotFound() {
      assertThat( GenericTypeParameterInvestigator.getTypeParameter( String.class, 0 ), nullValue() );
   }
   
   @Ignore @Test( expected = GenericTypeInvestigatorException.class )
   public void getTypeParameterOf_ThrowsExceptionWhen() {
      GenericTypeParameterInvestigator.getTypeParameter( String.class, 0 );
   }
}
