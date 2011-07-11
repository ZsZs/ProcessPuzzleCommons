package hu.itkodex.commons.xml;

import hu.itkodex.commons.text.TextComparator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class XmlDocumentComparator {
   private static final int PRETTY_PRINT_LINE_WIDHT = 160;
   private static final int PRETTY_PRINT_INDENT = 2;
   private static final char[] WHITESPACE_CHARACTERS = { ' ', '\t', '\n', '\r' };
   private static XmlFormatter formatter = new XmlFormatter( PRETTY_PRINT_INDENT, PRETTY_PRINT_LINE_WIDHT );

   public static boolean compareGeneratedDocumentToExpectedDocument( Document generatedDocument, Document expectedDocument ) throws IOException, DocumentException {

      String expectedDocumentAsText = convertDocumentToText( expectedDocument );
      String generatedDocumentAsText = convertDocumentToText( generatedDocument );

      return compareGeneratedDocumentToExpectedDocument( expectedDocumentAsText, generatedDocumentAsText );
   }

   public static boolean compareGeneratedDocumentToExpectedDocument( String generatedDocumentAsText, String expectedDocumentAsText ) throws IOException, DocumentException {
      String formattedGeneratedXml = formatXml( generatedDocumentAsText );
      String formattedExpectedXml = formatXml( expectedDocumentAsText );

      TextComparator comparator = new TextComparator( formattedGeneratedXml, formattedExpectedXml );

      if( comparator.isEqual() )
         return true;
      else{
         System.out.println( "Generated document:" );
         System.out.println( formattedGeneratedXml );
         System.out.println( "Generated document ends______________" );
         System.out.println( "Expected document:" );
         System.out.println( formattedExpectedXml );
         System.out.println( "Expected document ends______________" );
         System.out.println( "Expected document differs from:_" + comparator.differencesAsText() );
         int code = comparator.differentCharacter().codePointAt( 0 );
         System.out.println( "The character which caues the difference:_" + comparator.differentCharacter() + "_" + " and as ACII:" + code );
         System.out.println( "Generated document dump:\n" + dumpCharactesInString( formattedGeneratedXml ) );
         System.out.println( "Expected document dump:\n" + dumpCharactesInString( formattedExpectedXml ) );
         return false;
      }
   }

   public static String convertDocumentToText( Document document ) throws UnsupportedEncodingException, IOException {
      OutputFormat format = createPrettyPrintFormat();

      XMLWriter xmlWriter = new XMLWriter( format );

      OutputStream outputStream = new ByteArrayOutputStream();
      xmlWriter.setOutputStream( outputStream );

      xmlWriter.write( document );
      xmlWriter.flush();
      String documentAsText = outputStream.toString();
      documentAsText = formatXml( documentAsText );

      return documentAsText;
   }

   public static String dumpCharactesInString( String text ) {
      String dump = "";
      for( int index = 0; index < text.length(); index++ ){
         dump += text.substring( index, index + 1 ) + ":" + text.codePointAt( index ) + " ";
      }

      return dump;
   }

   // Protected, private helper methods
   private static OutputFormat createPrettyPrintFormat() {
      OutputFormat format = OutputFormat.createPrettyPrint();
      format.setNewLineAfterDeclaration( true );
      format.setLineSeparator( "\n" );
      format.setIndent( true );
      format.setIndentSize( PRETTY_PRINT_INDENT );

      return format;
   }

   /*
    * private static String formatXml( String xmlAsText ) throws IOException, DocumentException { OutputFormat format = createPrettyPrintFormat(); //
    * OutputStream outputStream = new ByteArrayOutputStream(); // xmlWriter.setOutputStream( outputStream ); StringWriter stringWriter = new StringWriter();
    * XMLWriter xmlWriter = new XMLWriter( stringWriter, format ); final org.dom4j.Document xmlDocument = org.dom4j.DocumentHelper.parseText( xmlAsText );
    * xmlWriter.write( xmlDocument ); xmlWriter.flush(); String formattedXml = stringWriter.toString(); formattedXml = removeLinefeeds( formattedXml ); return
    * formattedXml; }
    */
   private static String removeLineFeeds( String documentAsText ) {
      String strippedText = StringUtils.replace( documentAsText, "\r", "\n" );
      strippedText = StringUtils.replace( strippedText, "\n\n\n", "\n" );
      strippedText = StringUtils.replace( strippedText, "\n\n", "\n" );
      strippedText = StringUtils.replace( strippedText, " \n", "\n" );
      strippedText = StringUtils.replace( strippedText, " \n", "\n" );
 
      return strippedText;
   }

   public static String formatXml( String s ) {
      return formatter.format( s, 0 );
   }

   public static String formatXml( String s, int initialIndent ) {
      return formatter.format( s, initialIndent );
   }

   private static class XmlFormatter {
      private char currentChar;
      private int currentCharPosition;
      private int currentIndent;
      private XmlNodeTypes currentNodeType = XmlNodeTypes.Undefined;
      private StringBuilder formattedXml;
      private int indentNumChars;
      private int lineLength;
      private String sourceXml;

      public XmlFormatter( int indentNumChars, int lineLength ) {
         this.indentNumChars = indentNumChars;
         this.lineLength = lineLength;
      }

      public synchronized String format( String sourceXml, int initialIndent ) {
         currentIndent = initialIndent;
         formattedXml = new StringBuilder();
         this.sourceXml = removeLineFeeds( sourceXml );
         
         currentCharPosition = 0;
         while( sourceHasMoreCharacter( this.sourceXml ) ){
            currentChar = this.sourceXml.charAt( currentCharPosition );
            
            if( currentCharIsBeginningOfStartingTag() ) {
               indentTagStart(); currentNodeType = XmlNodeTypes.StartingTag;
            }else if( currentCharIsBeginningOfClosingTag() ) {
               currentNodeType = XmlNodeTypes.ClosingTag;
            }
            else if( currentCharIsBeginningOfTextNode() ) {
               indentTextNode(); currentNodeType = XmlNodeTypes.TextNode;
            }
            else if( currentCharIsSelfClosingTagEnd() ) {
               indentSelfClosingTagEnd(); currentNodeType = XmlNodeTypes.Undefined;
            }
            else if( currentCharIsTagEnd() ) {
               formatTagEnd(); currentNodeType = XmlNodeTypes.Undefined;
            }
            else if( currentCharIsUselessWhiteSpace() )
               spkipThisCharacter();
            else
               formattedXml.append( currentChar );
            
            currentCharPosition++;
         }
         return removeLineFeeds( formattedXml.toString() );
      }
      
      //Properties
      @SuppressWarnings( "unused" )
      public int getLineLength() { return lineLength; }

      //Private helper methods
      private boolean currentCharIsBeginningOfClosingTag() {
         return currentChar == '<' && sourceXml.charAt( currentCharPosition + 1 ) == '/';
      }
      
      private boolean currentCharIsBeginningOfStartingTag() {
         return currentChar == '<';
      }

      private boolean currentCharIsBeginningOfTextNode() {
         return !currentCharIsFirst() && !currentCharIsLast() && !currentCharIsUselessWhiteSpace() && previousCharIsTagEnd();
      }

      private boolean currentCharIsFirst() {
         return formattedXml.length() == 0;
      }

      private boolean currentCharIsLast() {
         boolean restOfTextIsWhiteSpace = StringUtils.containsOnly( StringUtils.substring( sourceXml, currentCharPosition +1 ), WHITESPACE_CHARACTERS );
         return currentCharPosition >= sourceXml.length() -1 || restOfTextIsWhiteSpace;
      }
      
      private boolean currentCharIsSelfClosingTagEnd() {
         return ( currentChar == '>' ) && ( sourceXml.charAt( currentCharPosition - 1 ) == '/' );
      }
      
      private boolean currentCharIsTagEnd() { return currentCharIsTagEnd( currentCharPosition ); }
      private boolean currentCharIsTagEnd( int investigatedCharacterPositon ) { 
         return sourceXml.charAt( investigatedCharacterPositon ) == '>'; 
      }
      
      private boolean currentCharIsTagStart() {
         return ( currentChar == '<' ) && ( sourceXml.charAt( currentCharPosition + 1 ) != '/' );
      }

      private boolean currentCharIsUselessWhiteSpace() {
         String restOfTextNode = StringUtils.substring( sourceXml, currentCharPosition, sourceXml.indexOf( '<', currentCharPosition));
         boolean restOfTextNodeIsWhiteSpace = ( currentNodeType == XmlNodeTypes.TextNode ) && StringUtils.containsOnly( restOfTextNode, WHITESPACE_CHARACTERS );
         return (( currentNodeType == XmlNodeTypes.Undefined ) && characterIsWhiteSpace( currentChar )) || restOfTextNodeIsWhiteSpace;
      }
      
      private boolean characterIsWhiteSpace( char characterToInvestige ){
         return StringUtils.containsOnly( Character.toString( characterToInvestige ), WHITESPACE_CHARACTERS ); 
      }

      private void decreaseIndentationLevel() {
         currentIndent -= indentNumChars;
      }

      private void formatTagEnd() {
          formattedXml.append( currentChar );
          if( !currentCharIsLast() )
             formattedXml.append( "\n" );
     }

      private void indentSelfClosingTagEnd() {
         decreaseIndentationLevel();
         formattedXml.append( currentChar );
         formattedXml.append( "\n" );
      }
      
      private void indentTagStart() {
         if( currentCharIsBeginningOfClosingTag() ){
            decreaseIndentationLevel();
            insertIndentation();
         }else if( currentCharIsTagStart() ){
            insertIndentation();
            increaseIndentationLevel();
         }
      }

      private void indentTextNode() {
         insertIndentation();
      }

      private void increaseIndentationLevel() {
         currentIndent += indentNumChars;
      }
      
      private void insertIndentation() {
         if( !currentCharIsFirst() ) formattedXml.append( "\n" );
         formattedXml.append( buildWhitespace( currentIndent ));
         formattedXml.append( currentChar );
      }

      private boolean previousCharIsTagEnd() {
         if( formattedXml.length() > 1 ){
            if( formattedXml.charAt( formattedXml.length() -1 ) == '>' ) return true;
            if( formattedXml.charAt( formattedXml.length() -1 ) == '\n' && formattedXml.charAt( formattedXml.length() -2 ) == '>' ) return true;
         }
         return false;
      }

      private void spkipThisCharacter() {
         // do nothing
      }

      private boolean sourceHasMoreCharacter( String sourceXml ) {
         return currentCharPosition < sourceXml.length();
      }
      
      private enum XmlNodeTypes {
         StartingTag,
         ClosingTag,
         SelftClosingTag,
         TextNode,
         Undefined
      }
   }

   private static String buildWhitespace( int numChars ) {
      StringBuilder sb = new StringBuilder();
      for( int i = 0; i < numChars; i++ )
         sb.append( " " );
      return sb.toString();
   }

   /**
    * Wraps the supplied text to the specified line length.
    * 
    * @lineLength the maximum length of each line in the returned string (not including indent if specified).
    * @indent optional number of whitespace characters to prepend to each line before the text.
    * @linePrefix optional string to append to the indent (before the text).
    * @returns the supplied text wrapped so that no line exceeds the specified line length + indent, optionally with indent and prefix applied to each line.
    */
   @SuppressWarnings( "unused" )
   private static String lineWrap( String s, int lineLength, Integer indent, String linePrefix ) {
      if( s == null )
         return null;

      StringBuilder sb = new StringBuilder();
      int lineStartPos = 0;
      int lineEndPos;
      boolean firstLine = true;
      while( lineStartPos < s.length() ){
         if( !firstLine )
            sb.append( "\n" );
         else
            firstLine = false;

         if( lineStartPos + lineLength > s.length() )
            lineEndPos = s.length() - 1;
         else{
            lineEndPos = lineStartPos + lineLength - 1;
            while( lineEndPos > lineStartPos && (s.charAt( lineEndPos ) != ' ' && s.charAt( lineEndPos ) != '\t') )
               lineEndPos--;
         }
         sb.append( buildWhitespace( indent ) );
         if( linePrefix != null )
            sb.append( linePrefix );

         sb.append( s.substring( lineStartPos, lineEndPos + 1 ) );
         lineStartPos = lineEndPos + 1;
      }
      return sb.toString();
   }
}
