package hu.itkodex.commons.file;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileHelper {
   protected static Logger log = LoggerFactory.getLogger( FileHelper.class );

   public static boolean checkFileExist( String filePath ) {
      File fileToCheck = new File( filePath );
      if( fileToCheck.exists() )
         return true;
      else
         return false;
   }

   public static void deleteFile( String filePath ) {
      File fileToDelete = new File( filePath );
      fileToDelete.delete();
   }

   public static String educeRealPathFromClassPath( String relativeFilePath ) {
      if( new File( relativeFilePath ).exists() ) return relativeFilePath;
      
      URL url = ClassLoader.getSystemResource( relativeFilePath );
      String enducedRealPath = null;

      if( url != null ){
         enducedRealPath = url.getPath();
         if( StringUtils.startsWithIgnoreCase( enducedRealPath, "/" ) )
            enducedRealPath = StringUtils.substringAfter( enducedRealPath, "/" );
      }

      return enducedRealPath;
   }

   public static String folderOf( String filePath ) {
      return FilenameUtils.getFullPath( filePath );
   }

   public static File openOrCreateNewFile( String fileFullPath ) {
      File targetFile = new File( fileFullPath );
      if( !targetFile.exists() ){
         try{
            if( isFolderName( fileFullPath ) ) 
               FileUtils.forceMkdir( targetFile );
            else 
               targetFile.createNewFile();
         }catch( IOException e ){
            if( FilenameUtils.getPath( fileFullPath ) != null && FilenameUtils.getPath( fileFullPath ) != "" ){
               openOrCreateNewFile( FilenameUtils.getFullPath( fileFullPath ) );
               targetFile = openOrCreateNewFile( fileFullPath );
            }
            else {
               log.error( "Error in creation of file: " + fileFullPath, e );
               targetFile = null;
            }
         }
      }
      return targetFile;
   }
   
   public static File openOrCreateNewFile( String baseFolderFullPath, String fileSubPathWithName ){
      return openOrCreateNewFile( FilenameUtils.concat( baseFolderFullPath, fileSubPathWithName ));
   }

   public static String retrieveFileContentAsString( String filePath ) {
      String educedFilePath = educeRealPathFromClassPath( filePath );
      if( educedFilePath == null ) return null;
      
      String fileContent = null;
      
      File sourceFile = new File( educedFilePath );
      try{
         fileContent = FileUtils.readFileToString( sourceFile );
      }catch( IOException e ){
         log.debug( "Reading text file: '" + educedFilePath + "' failed.");
      }
      
      return fileContent;
   }

   public static boolean isFolderName( String fileSystemResourceName ) {
      if( FilenameUtils.getName( fileSystemResourceName ).equals( "" )) return true;
      else return false;
   }
}
