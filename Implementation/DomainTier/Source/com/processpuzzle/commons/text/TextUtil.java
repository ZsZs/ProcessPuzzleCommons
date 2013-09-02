package com.processpuzzle.commons.text;

import org.apache.commons.lang.StringUtils;

public class TextUtil {
   public static String insertTextInto( String textToInsert, String textInsertInto, String insertionPointSpecifier ) {
      int insertionPoint = textInsertInto.indexOf( insertionPointSpecifier );
      String resultText = StringUtils.left( textInsertInto, insertionPoint );
      resultText += textToInsert;
      resultText += textInsertInto.substring( insertionPoint + insertionPointSpecifier.length() );
      return resultText;
   }

   public static String extractText( String textToInvestigate, String precedingText, String followingText ) {
      String extractedText = null;
      int startingPosition = textToInvestigate.toLowerCase().indexOf( precedingText.toLowerCase() ) + precedingText.length();
      int endingPosition = textToInvestigate.toLowerCase().indexOf( followingText.toLowerCase(), startingPosition );
      
      if( startingPosition != -1 && endingPosition != -1 ) {
         extractedText = textToInvestigate.substring( startingPosition, endingPosition );
         extractedText = StringUtils.strip( extractedText );
      }
      
      return extractedText;
   }
}
