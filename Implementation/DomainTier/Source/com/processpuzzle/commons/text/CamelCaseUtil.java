package com.processpuzzle.commons.text;

import org.apache.commons.lang.StringUtils;

public class CamelCaseUtil {

   public static String convertToCamelCase( String sourceText ) {
      return convertToCamelCase( sourceText, true );
   }
   
   public static String convertToCamelCase( String sourceText, boolean lowerFirstLetter ) {
      StringBuffer resultTextBuffer = new StringBuffer( sourceText.length() );
      String stippedSourceText = StringUtils.strip( sourceText );
      String firstLetter = stippedSourceText.substring( 0, 1 );
         
      boolean whiteSpaceFound = false;
      for( int i = 0; i < stippedSourceText.length(); i++ ){
         char c = stippedSourceText.charAt( i );
         if( c == ' ' ){
            whiteSpaceFound = true;
         }else{
            if( whiteSpaceFound ){
               resultTextBuffer.append( stippedSourceText.substring( i, i + 1 ).toUpperCase() );
               whiteSpaceFound = false;
            }else resultTextBuffer.append( c );
         }
      }
      
      if( lowerFirstLetter ) resultTextBuffer.setCharAt( 0, firstLetter.toLowerCase().charAt( 0 ) );
      
      return resultTextBuffer.toString();
   }

}
