package com.processpuzzle.commons.spring;

public class BeanName {

   public static <F> String determineBeanNameFromClass( Class<F> beanClass ) {
      String firstCharacter = beanClass.getSimpleName().substring( 0, 1 ).toLowerCase();
      String beanName = firstCharacter + beanClass.getSimpleName().substring( 1 );
      return beanName;
   }
}
