package com.processpuzzle.commons.file;

import static com.processpuzzle.commons.file.FileExist.isExistingFile;
import static com.processpuzzle.commons.file.FileNotExist.notExistingFile;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import com.processpuzzle.commons.file.FileHelper;

public class FileHelperTest {
   private static final String DUMMY_FILE_NAME = "dummy file name.txt";
   private static final String FILE_TO_CREATE = "FileHelperTests/FileCreatedByFileHelperTest.txt";
   private static final String TEMP_FOLDER = "C:/temp/";
   private static final String TEST_FILE_CONTENT = "\r\nHello world! \r\n\r\n";
   private static final String TEST_FILE_NAME = "SimpleTextFile.txt";
   private static final String TEST_FILE_FOLDER = "com/processpuzzle/commons/file/";
   private static final String TEST_FILE_PATH = TEST_FILE_FOLDER + TEST_FILE_NAME;
   private String testFileRealPath;
   private String testFileFolderRealPath;
   
   @Before
   public void beforeEachTests() {
      testFileRealPath = StringUtils.substringAfter( ClassLoader.getSystemResource( TEST_FILE_PATH ).getPath(), "/" );
      testFileFolderRealPath = FilenameUtils.getFullPath( testFileRealPath );
   }
   
   @Test
   public void checkFileExist_WhenFileExist_ReturnsTrue(){
      assertThat( FileHelper.checkFileExist( testFileRealPath ), is( true ));
   }
   
   @Test
   public void checkFileExist_WhenFileNotExist_ReturnsFalse(){
      assertThat( FileHelper.checkFileExist( DUMMY_FILE_NAME ), is( false ));
   }
   
   @Test
   public void educeRealPathFromClassPath_WhenFileExist_AddsRealPathToRelative() {
      assertThat( FileHelper.educeRealPathFromClassPath( TEST_FILE_PATH ), equalTo( testFileRealPath ));
   }
   
   @Test
   public void educeRealPathFromClassPath_WhenRealPathIsGiven_ReturnsPathBack() {
      assertThat( FileHelper.educeRealPathFromClassPath( testFileRealPath ), equalTo( testFileRealPath ));
   }
   
   @Test
   public void educeRealPathFromClassPath_WhenFileNotExist_ReturnsNull() {
      assertThat( FileHelper.educeRealPathFromClassPath( DUMMY_FILE_NAME ), is( nullValue() ));
   }
   
   @Test
   public void folderOf_WhenFileExist_ReturnsParentFolderName() {
      assertThat( FileHelper.folderOf( testFileRealPath ), equalTo( testFileFolderRealPath ));
   }
   
   @Test
   public void openOrCreateNewFile_WhenFileExist_ReturnsFile() {
      assertThat( FileHelper.openOrCreateNewFile( testFileRealPath ), is( notNullValue() ));
      assertThat( FileHelper.openOrCreateNewFile( testFileRealPath ), instanceOf( File.class ));
   }
   
   @Test
   public void openOrCreateNewFile_WhenFileExistAndFileNameFolderNameIsGiven_ReturnsFile() {
      assertThat( FileHelper.openOrCreateNewFile( testFileFolderRealPath, TEST_FILE_NAME ), is( notNullValue() ));
      assertThat( FileHelper.openOrCreateNewFile( testFileFolderRealPath, TEST_FILE_NAME ), instanceOf( File.class ));
   }
   
   @Test
   public void openOrCreateNewFile_WhenFileNotExist_ReturnsNewFile() {
      //SETUP:
      String filePathToCreate = TEMP_FOLDER + FILE_TO_CREATE;
      assumeThat( filePathToCreate, notExistingFile() );
      
      //EXCERCISE:
      File createdFile = FileHelper.openOrCreateNewFile( filePathToCreate );
      
      //VERIFY:
      assertThat( createdFile, is( notNullValue() ));
      assertThat( filePathToCreate, isExistingFile() );
      
      //TEAR DOWN:
      FileHelper.deleteFile( filePathToCreate );
      FileHelper.deleteFile( FilenameUtils.getFullPath( filePathToCreate ) );
   }
   
   @Test
   public void openOrCreateNewFile_WhenFileNotExistAndFileNameFolderNameIsGiven_ReturnsNewFile() {
      //SETUP:
      String filePathToCreate = TEMP_FOLDER + FILE_TO_CREATE;
      assumeThat( filePathToCreate, notExistingFile() );
      
      //EXCERCISE:
      File createdFile = FileHelper.openOrCreateNewFile( TEMP_FOLDER, FILE_TO_CREATE );
      
      //VERIFY:
      assertThat( createdFile, is( notNullValue() ));
      assertThat( filePathToCreate, isExistingFile() );
      
      //TEAR DOWN:
      FileHelper.deleteFile( filePathToCreate );
      FileHelper.deleteFile( FilenameUtils.getFullPath( filePathToCreate ) );
   }
   
   @Test
   public void retrieveFileContentAsString_WhenFileExist_ReturnsString() throws IOException, DocumentException {
      assertThat( FileHelper.retrieveFileContentAsString( testFileRealPath ), equalTo( TEST_FILE_CONTENT ));
   }
   
   @Test
   public void retrieveFileContentAsString_WhenFileExist_EduceRealPath() throws IOException, DocumentException {
      assertThat( FileHelper.retrieveFileContentAsString( TEST_FILE_PATH ), equalTo( TEST_FILE_CONTENT ));
   }
   
   @Test
   public void retrieveFileContentAsString_WhenFileNotExist_ReturnsNull() {
      assertThat( FileHelper.retrieveFileContentAsString( DUMMY_FILE_NAME ), is( nullValue() ));
   }
}
