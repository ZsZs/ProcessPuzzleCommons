package com.processpuzzle.commons.file;

import java.io.File;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class FileExist extends TypeSafeMatcher<String> {

   @Override
   protected boolean matchesSafely( String filePath ) {
      return new File( filePath ).exists();
   }

   @Override
   public void describeTo( Description description ) {
      description.appendText("file exist");
   }

   @Factory
   public static <T> Matcher<String> isExistingFile() {
      return new FileExist();
   }
}
