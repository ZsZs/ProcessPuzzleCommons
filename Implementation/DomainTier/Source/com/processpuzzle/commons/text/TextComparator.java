package com.processpuzzle.commons.text;


import java.util.LinkedList;

import org.apache.commons.lang.StringUtils;

import com.processpuzzle.commons.text.TextDifferencer.Diff;


public class TextComparator {
   private String textOne;
   private String textTwo;
   private TextDifferencer differenceMatcher;
   private LinkedList<Diff> difference;
   private String differenceAsText;
   
   public TextComparator( String textOne, String textTwo ) {
      this.textOne = textOne;
      this.textTwo = textTwo;
      
      differenceMatcher = new TextDifferencer();
      difference = differenceMatcher.diff_main( this.textOne, this.textTwo );
      
      differenceAsText = StringUtils.difference( textOne, textTwo );
   }
   
   public String differencesAsText() {
      return differenceAsText;
   }

   public String differentCharacter() {
      int differenceIndex = textTwo.length() - differenceAsText.length();
      return textOne.substring( differenceIndex, differenceIndex +1 );
   }

   public boolean isEqual() {
      if( differenceAsText == "" ) return true;
      else return false;
   }

   public boolean printOutDifference() {
      System.out.println( "Number of differences:" + difference.size() );
      int index = 1;
      
      for( Diff diff : difference ) {
         System.out.println( "Difference:" + index++ + diff.text );
      }
      
      return false;
   }
}
