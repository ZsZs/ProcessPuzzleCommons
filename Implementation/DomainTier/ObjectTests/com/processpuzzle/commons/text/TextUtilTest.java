package com.processpuzzle.commons.text;


import org.junit.Test;

import com.processpuzzle.commons.text.TextUtil;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.*;

public class TextUtilTest {
   @Test public void insertTextInto_ReplacesInsertionPointSpecifier() {
      String textInsertInto = "Hello <insert text>!";
      String textToInsert = "world";
      String insertionPointSpecifier = "<insert text>";
      
      assertThat( TextUtil.insertTextInto( textToInsert, textInsertInto, insertionPointSpecifier ), equalTo( "Hello world!" ));
   }
   
   @Test public void extractText_FindsTextBetweenPreceedingAndFollowingText() {
      String textToInvestigate = "This text preceeds the text to look for.";
      String precedingText = "This text preceeds";
      String followingText = ".";
      
      assertThat( TextUtil.extractText( textToInvestigate, precedingText, followingText ), equalTo( "the text to look for" ));
   }
   
   @Test public void extractText_IsCaseInsensitive() {
      String textToInvestigate = "This text preceeds the text to look for.";
      String precedingText = "this text preceeds";
      String followingText = ".";
      
      assertThat( TextUtil.extractText( textToInvestigate, precedingText, followingText ), equalTo( "the text to look for" ));      
   }
   
   @Test public void extractText_StripsExtractedText() {
      String textToInvestigate = "This text preceeds    the text to look for   .";
      String precedingText = "this text preceeds";
      String followingText = ".";
      
      assertThat( TextUtil.extractText( textToInvestigate, precedingText, followingText ), equalTo( "the text to look for" ));      
   }
   
   @Test public void extractText_NeglectsFollowingTextInPrecedingText() {
      String textToInvestigate = "This text preceeds...    the text to look for   .";
      String precedingText = "this text preceeds...";
      String followingText = ".";
      
      assertThat( TextUtil.extractText( textToInvestigate, precedingText, followingText ), equalTo( "the text to look for" ));      
   }
}
