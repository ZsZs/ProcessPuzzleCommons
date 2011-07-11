package hu.itkodex.commons.generics;

import java.lang.reflect.ParameterizedType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericTypeParameterInvestigator {
   private static Logger logger = LoggerFactory.getLogger( GenericTypeParameterInvestigator.class );
   
   //This interface is misleading. There is no way to determine a runtime object's actual parameters.
//   public static Class<?> getTypeParameter( Object subjectObject, int parameterIndex ) {
//      Class<?> parameterType = null;
//      parameterType = getTypeParameterOf( subjectObject.getClass(), parameterIndex ); 
//      return parameterType;
//   }
   
   public static Class<?> getTypeParameter( Class<?> genericType, int parameterIndex ) {
      ParameterizedType parametrizedType = null;
      Class<?> parameterType = null;
      logger.trace( "Intending to investigate:'" + genericType.getName() + "'" );
      
      try{
         parametrizedType = ((ParameterizedType) genericType.getGenericSuperclass()); 
         parameterType = (Class<?>) parametrizedType.getActualTypeArguments()[parameterIndex];
      }catch( ClassCastException e ){
         if( parametrizedType != null && parametrizedType.getActualTypeArguments()[parameterIndex] instanceof ParameterizedType )
            return (Class<?>) ((ParameterizedType) parametrizedType.getActualTypeArguments()[parameterIndex]).getRawType();
            
         java.lang.reflect.Type superClass = genericType.getGenericSuperclass();
         if( !(superClass instanceof ParameterizedType )) {
            if( !(superClass.equals( Object.class )))
               //Type is not generic but probable it's parent is.
               return GenericTypeParameterInvestigator.getTypeParameter( (Class<?>) superClass, parameterIndex );
         }
      }catch( ArrayIndexOutOfBoundsException e ) {
         //Check if it's parent has this parameter.
         java.lang.reflect.Type superClass = genericType.getGenericSuperclass();
         if( !(superClass instanceof ParameterizedType )) {
            if( !(superClass.equals( Object.class )))
               //Type is not generic but probable it's parent is.
               return GenericTypeParameterInvestigator.getTypeParameter( (Class<?>) superClass, parameterIndex );
         }else throw new GenericTypeInvestigatorException( genericType, e );
      }catch( Exception e ){
         logger.error( "Exception occured when trying to detemine generic parameter type of: '" + genericType.getName() + "' type");
         throw new GenericTypeInvestigatorException( genericType, e );
      }
      return parameterType;      
   }   
}
