package hu.itkodex.commons.compiler;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.typeCompatibleWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import org.junit.Before;
import org.junit.Test;

import com.vladium.utils.ClassScope;

public class StringCompilerTest {
   private static final String sourceCode = "" 
      + "package hu.itkodex.commons.compiler;"
      + "public class TestClassForStringCompiler implements TestInterfaceForStringCompiler {}"; 
   private static final String anotherSourceCode = "" 
      + "package hu.itkodex.commons.compiler;"
      + "public class AnotherTestClassForStringCompiler extends TestClassForStringCompiler {}"; 
   private StringCompiler<TestInterfaceForStringCompiler> defaultCompiler;
   private Class<? extends TestInterfaceForStringCompiler> compiledClass;
   
   @Before
   public void beforeEachTests() throws ClassCastException, StringCompilerException {
      defaultCompiler = new StringCompiler<TestInterfaceForStringCompiler>( null, null );
      compiledClass = defaultCompiler.compile( sourceCode, null, new Class[] { TestInterfaceForStringCompiler.class } );      
   }

   @Test
   public void constructor_HasDefaults() throws ClassCastException, StringCompilerException, InstantiationException, IllegalAccessException {
      assertThat( defaultCompiler.isSuccess(), is( true ) );
   }

   @Test public void compile_LoadsNewClass() throws ClassCastException, StringCompilerException, ClassNotFoundException {
      assertThat( classIsLoaded( defaultCompiler.getClassLoader(), compiledClass ), is( true ) );
   }
   
   @Test( expected = ClassNotFoundException.class )
   public void compile_DoesNotAddClassToClassPath() throws ClassNotFoundException {
      Class.forName( compiledClass.getName() );
   }

   @Test public void compile_CompiledClassCanBeInstantiated() throws ClassCastException, StringCompilerException, InstantiationException, IllegalAccessException {
      TestInterfaceForStringCompiler newInstance = compiledClass.newInstance();

      assertThat( newInstance, notNullValue() );
      assertThat( newInstance, instanceOf( TestInterfaceForStringCompiler.class ) );
   }
   
   @Test( expected = ClassCastException.class )
   public void compile_ChecksCastability() throws ClassCastException, StringCompilerException {
      defaultCompiler.compile( sourceCode, null, new Class[] { NotSuperclassOfTestClass.class } );
   }
   
   @Test
   public void compile_AddsPreviousCompilationToTheClassPath() throws ClassCastException, StringCompilerException {
      assumeThat( defaultCompiler.isSuccess(), is( true ) );
      
      StringCompiler<TestInterfaceForStringCompiler> anotherCompiler = new StringCompiler<TestInterfaceForStringCompiler>( null, null, defaultCompiler );
      Class<? extends TestInterfaceForStringCompiler> anotherCompiledClass = anotherCompiler.compile( anotherSourceCode, null, new Class[] { TestInterfaceForStringCompiler.class } );      
      
      assertThat( anotherCompiler.isSuccess(), is( true ) );
      assertThat( classIsLoaded( anotherCompiler.getClassLoader(), anotherCompiledClass ), is( true ));
      assertThat( anotherCompiledClass, typeCompatibleWith( compiledClass ));
   }

   private Boolean classIsLoaded( ClassLoader classLoader, Class<?> classToCheck ) {
      for( Class<?> clazz : ClassScope.getLoadedClasses( classLoader ) ){
         if( clazz.equals( classToCheck ) ) return true;
      }
      return false;
   }
}
