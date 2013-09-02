package hu.itkodex.commons.fitnesse.generics.domain;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

public class TypeParameterInvestigatorTest {
   private static final String TEST_CLASS_SOURCE = ""
      + "package hu.itkodex.commons.fitnesse.generics.domain;"
      + "import java.util.Date;"
      + "import com.processpuzzle.commons.generics.TestParametrizedClass;"
      + "public class TestParametrizedSubclass extends TestParametrizedClass<String, Date> {}";
   private TypeParameterInvestigator parameterInvestigator;
   private String parameterType;
   
   @Before public void beforeEachTests() throws FileNotFoundException {
      parameterInvestigator = new TypeParameterInvestigator();
      parameterInvestigator.sourceCode = TEST_CLASS_SOURCE;
      parameterInvestigator.parameterIndex = 0;
      parameterType = parameterInvestigator.parameterTypeIs();
   }
   
   @Test public void classAndPackageNameIsDetermined() {
      assertThat( parameterInvestigator.getParametrizedClassName(), equalTo( "TestParametrizedSubclass" ));
      assertThat( parameterInvestigator.getParametrizedClassPackage(), equalTo( "hu.itkodex.commons.fitnesse.generics.domain" ));
   }
   
   @Test public void classIsCompiled() {
      assertThat( parameterInvestigator.getParametrizedClassFile(), notNullValue() );
   }
   
   @Test public void classIsLoaded() throws ClassNotFoundException {
      assertThat( parameterInvestigator.getParametrizedClass(), notNullValue() );
   }
   
   @Test public void parameterTypeIs() {
      assertThat( parameterType, equalTo( "java.lang.String" ));
   }
}
