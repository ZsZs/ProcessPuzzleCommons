package hu.itkodex.commons.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class SourceCodeTemplateEnhancer {
   public static final String IMPORT_PLACEHOLDER = "<<import>>";
   public static final String EXPRESSIONS_PLACEHOLDER = "<<expressions>>";
   public static final String FIELDS_PLACEHOLDER = "<<fields>>";
   public static final String MATCHER_PLACEHOLDER = "<<macher>>";
   public static final String OBJECT_PLACEHOLDER = "<<subject>>";
   public static final String TYPE_PLACEHOLDER = "<<type>>";
   public static final String VARIABLE_PLACEHOLDER = "<<variable>>";
   private static final Logger logger = LoggerFactory.getLogger( SourceCodeTemplateEnhancer.class );
   private String sourceCodePath;
   private String sourceCodeTemplate;
   
   public SourceCodeTemplateEnhancer( String sourceCodePath ) {
      this.sourceCodePath = sourceCodePath;
      if( this.sourceCodePath != null ) loadSourceFromFile();
   }
   
   public String createFieldNameFromMethod( Method getterMethod ) {
      String fieldName = null;
      fieldName = getterMethod.getName().substring( 3 ); 
      String firstCharacter = fieldName.substring( 0, 1 ).toLowerCase();
      fieldName = firstCharacter + fieldName.substring( 1 );
   
      return fieldName;
   }

   public void finalizeSourceCode() {
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, IMPORT_PLACEHOLDER, "" );
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, EXPRESSIONS_PLACEHOLDER, "" );
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, FIELDS_PLACEHOLDER, "" );
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, MATCHER_PLACEHOLDER, "" );
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, OBJECT_PLACEHOLDER, "" );
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, TYPE_PLACEHOLDER, "" );
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, VARIABLE_PLACEHOLDER, "" );
   }
   
   public void insertExpressions( String expressions ) {
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, EXPRESSIONS_PLACEHOLDER, expressions );
   }
   
   public void insertFieldFromGetter( Method getterMethod ) {
      String fieldDeclaration = "";
      
      if( !StringUtils.startsWith( getterMethod.getName(), "get" ))
         throw new RuntimeException( "Method should start with ''get''.");
      
      String fieldName = createFieldNameFromMethod( getterMethod );
      fieldDeclaration += "   public " + getterMethod.getReturnType().getName() + " " + fieldName + ";\n" + FIELDS_PLACEHOLDER;
      this.insertFields( fieldDeclaration );
   }
   
   public void insertFields( String fieldsDeclarations ) {
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, FIELDS_PLACEHOLDER, fieldsDeclarations );
   }
   
   public void insertImportIntoSource( Class<?> subjectClass ) {
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, IMPORT_PLACEHOLDER, "import " + subjectClass.getName() + ";" );
   }

   //Public accessors and mutators
   public void loadSourceFromFile() {
      ResourceLoader resourceLoader = new DefaultResourceLoader();
      Resource sourceCodeResource = resourceLoader.getResource( sourceCodePath );
      InputStream inputStream = null;
      try{
         inputStream = sourceCodeResource.getInputStream(); 
         sourceCodeTemplate = IOUtils.toString( inputStream );
      }catch( IOException e ){
         String message = "Couldn't read in file:" + sourceCodePath;
         logger.error( message );
         throw new RuntimeException( message, e );
      }finally {
         IOUtils.closeQuietly( inputStream );
      }
   }

   public void replaceGenericType( String genericTypeName ) {
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, TYPE_PLACEHOLDER, genericTypeName );
   }

   public void replaceMatcherExpression( String matcherExpression ) {
      sourceCodeTemplate = StringUtils.replace( sourceCodeTemplate, MATCHER_PLACEHOLDER, matcherExpression );
   }

   public void replaceObjectExpression( String objectExpression ) {
      sourceCodeTemplate = StringUtils.replace(  sourceCodeTemplate, OBJECT_PLACEHOLDER, objectExpression );
   }

   public void replaceVariable( String variableName ) {
      sourceCodeTemplate = StringUtils.replace(  sourceCodeTemplate, VARIABLE_PLACEHOLDER, variableName );
   }
   
   //Properties
   public String getSourceCodeTeamplate() {
      return sourceCodeTemplate;
   }
   
   //Protected, private helper methods

}
