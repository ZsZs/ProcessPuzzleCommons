package com.processpuzzle.commons.xml;


import org.junit.Test;

import com.processpuzzle.commons.xml.XmlDocumentComparator;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

public class XmlDocumentComparatorTest {
   private static final String ONE_LEVEL_XML = "<tag_1 attribute='some attr value'/><tag_1 attribute='other attr value'></tag_1>";
   private static final String ONE_LEVEL_XML_FORMATTED = "<tag_1 attribute='some attr value'/>\n<tag_1 attribute='other attr value'>\n</tag_1>";
   private static final String ONE_LEVEL_XML_WITH_TEXT = "<tag_1>level_1 text</tag_1>";
   private static final String ONE_LEVEL_XML_WITH_TEXT_FORMATTED = "<tag_1>\n  level_1 text\n</tag_1>";
   private static final String TWO_LEVEL_XML = "<tag_1>level_1 text<tag_2>level_2 text</tag_2></tag_1>";
   private static final String TWO_LEVEL_XML_FORMATTED = "<tag_1>\n  level_1 text\n  <tag_2>\n    level_2 text\n  </tag_2>\n</tag_1>";
   private static final String COMPLEX_XML = "<div id='widgetContainer'><div id='parent'></div><span></span></div>";
   private static final String COMPLEX_XML_FORMATTED = "<div id='widgetContainer'>\n  <div id='parent'>\n  </div>\n  <span>\n  </span>\n</div>";
   private static final String WELL_FORMATTED_XML = "<div id='widgetContainer'>\n  <div id='parent'>\n  </div>\n  <span>\n  </span>\n</div>";
   private static final String XML_WITH_WHITE_SPACES = "\n  \n\n<tag_1>level_1 text\n     <tag_2>level_2 text</tag_2></tag_1>\n  \n\n";

   @Test
   public void formatXml_breaksEachTags() {
      assertThat( XmlDocumentComparator.formatXml( ONE_LEVEL_XML ), equalTo( ONE_LEVEL_XML_FORMATTED ));
   }
   
   @Test
   public void formatXml_indentsTextNodes() {
      assertThat( XmlDocumentComparator.formatXml( ONE_LEVEL_XML_WITH_TEXT ), equalTo( ONE_LEVEL_XML_WITH_TEXT_FORMATTED ));
   }
   
   @Test
   public void formatXml_indentsMultiLevel() {
      assertThat( XmlDocumentComparator.formatXml( TWO_LEVEL_XML ), equalTo( TWO_LEVEL_XML_FORMATTED ));
   }
   
   @Test
   public void formatXml_removesPreviousWhiteSpaces() {
      assertThat( XmlDocumentComparator.formatXml( XML_WITH_WHITE_SPACES ), equalTo( TWO_LEVEL_XML_FORMATTED ));
   }
   
   @Test
   public void formatXml_preservesWellFormattedSource() {
      assertThat( XmlDocumentComparator.formatXml( WELL_FORMATTED_XML ), equalTo( WELL_FORMATTED_XML ));
   }
   
   @Test
   public void formatXml_handlesComplexStructures() {
      assertThat( XmlDocumentComparator.formatXml( COMPLEX_XML ), equalTo( COMPLEX_XML_FORMATTED ));
   }
}
