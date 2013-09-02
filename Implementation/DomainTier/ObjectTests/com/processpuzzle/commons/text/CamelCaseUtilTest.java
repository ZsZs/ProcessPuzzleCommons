package com.processpuzzle.commons.text;


import org.junit.Test;

import com.processpuzzle.commons.text.CamelCaseUtil;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.*;

public class CamelCaseUtilTest {
   @Test public void convertToCamelCase_RemovesSpacesAndCapitalizeWordsFirstLetters() {
      assertThat( CamelCaseUtil.convertToCamelCase( " any text with spaces " ), equalTo( "anyTextWithSpaces" ));
      assertThat( CamelCaseUtil.convertToCamelCase( "Any Text With Spaces" ), equalTo( "anyTextWithSpaces" ));
      assertThat( CamelCaseUtil.convertToCamelCase( "anyTextWithSpaces" ), equalTo( "anyTextWithSpaces" ));
   }
}
