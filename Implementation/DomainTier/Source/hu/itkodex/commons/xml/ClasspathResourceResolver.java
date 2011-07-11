package hu.itkodex.commons.xml;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class ClasspathResourceResolver implements LSResourceResolver {
   protected Logger log = LoggerFactory.getLogger( ClasspathResourceResolver.class );
   private DOMImplementationLS domImplementationLS;

   public ClasspathResourceResolver() throws ClassNotFoundException, IllegalAccessException, InstantiationException {
      System.setProperty( DOMImplementationRegistry.PROPERTY, "org.apache.xerces.dom.DOMImplementationSourceImpl" );
      DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
      domImplementationLS = (DOMImplementationLS) registry.getDOMImplementation( "LS" );
   }

   public LSInput resolveResource( String type, String namespaceURI, String publicId, String systemId, String baseURI ) {
      log.info( "==== Resolving '" + type + "' '" + namespaceURI + "' '" + publicId + "' '" + systemId + "' '" + baseURI + "'" );

      String schemaPath = "classpath:" + StringUtils.stripStart( systemId, "../"  );
      ResourceLoader resourceLoader = new DefaultResourceLoader();
      Resource schemaResource = resourceLoader.getResource( schemaPath );
      
      LSInput lsInput = domImplementationLS.createLSInput();
      try{
         lsInput.setByteStream( schemaResource.getInputStream() );
      }catch( IOException e ){
         log.debug( e.getLocalizedMessage() );
      }
      lsInput.setSystemId( systemId );
      return lsInput;
   }
}
