package hu.itkodex.commons.xml;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.xml.sax.SAXException;

public class ObjectXmlBinder {
   
   public org.dom4j.Document marshallObjectToXml( Object objectToMarshall, String schemaPath ) {
      DocumentResult documentResult = new DocumentResult();
      Document viewAsXml = null;
      
      try{
         JAXBContext jaxbContext = JAXBContext.newInstance( objectToMarshall.getClass() );
         Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
         jaxbMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
         Schema schema = loadSchema( schemaPath );
         jaxbMarshaller.setSchema( schema );
         jaxbMarshaller.marshal( objectToMarshall, documentResult );
         viewAsXml = documentResult.getDocument();
         
         //System.out.println( viewAsXml.asXML() );
      }catch( JAXBException e ){
         e.printStackTrace();
      }
      
      return viewAsXml;
      
   }

   public org.dom4j.Document readDocumentFromClassPath( String expectedXmlPath ) {
      ResourceLoader resourceLoader = new DefaultResourceLoader();
      Resource resource = resourceLoader.getResource( expectedXmlPath );
      SAXReader saxReader = new SAXReader();
      org.dom4j.Document expectedXml = null;
      try{
         expectedXml = saxReader.read( resource.getInputStream() );
      }catch( DocumentException e ){
         e.printStackTrace();
      }catch( IOException e ){
         e.printStackTrace();
      }
      
      return expectedXml;
   }
   
   private Schema loadSchema( String schemaPath ) {
      Schema artifactSchema = null;
      ResourceLoader resourceLoader = new DefaultResourceLoader(); 

      SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
   
      try{
         factory.setResourceResolver( new ClasspathResourceResolver() );
         factory.setErrorHandler( new ObjectBinderErrorHandler( schemaPath ));
         Resource schemaResource = resourceLoader.getResource( schemaPath );
         StreamSource schemaSource = new StreamSource( schemaResource.getInputStream() );
         artifactSchema = factory.newSchema( schemaSource );
      }catch( SAXException e ){
         e.printStackTrace();
      }catch( IOException e ){
         e.printStackTrace();
      }catch( Exception e ){
         e.printStackTrace();
      }
      return artifactSchema;
   }
}
