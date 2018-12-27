package com.example.demo.drive;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.example.demo.dao.FileStorageDao;
import com.example.dto.FileResponse;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FilesInGoogleDriveTest {

	@Mock
	private FileStorageDao fileStorageDaoMock;
	
	@InjectMocks
	private FilesInGoogleDrive filesInDrive;
	
	@Test
	public void testCreateFileInGoogleDriveValidFile() {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.doc", "text/plain",
				"test data".getBytes());
		Mockito.when(fileStorageDaoMock.save(Mockito.any())).thenReturn(1);
		
		FileResponse actual =filesInDrive.createFileInGoogleDrive(mockMultipartFile);
		
		assertEquals("test.doc", actual.getFileName());
		
		
	}

	@Test
	public void testCreateFileInGoogleDriveInvalidFile() {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.text", "text/plain",
				"test data".getBytes());
		
		FileResponse actual = filesInDrive.createFileInGoogleDrive(mockMultipartFile);
		
		assertEquals("test.text", actual.getFileName());
	}
	
	@Test
	public void testFindFileInGoogleDriveValidFile() {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.doc", "text/plain",
				"test data".getBytes());
		Mockito.when(fileStorageDaoMock.save(Mockito.any())).thenReturn(1);
		
		String actualResult = filesInDrive.findFileInGoogleDrive(mockMultipartFile);
		
		assertEquals("File Exists in Google Drive test.doc", actualResult);
	}
	
	@Test
	public void testFindFileInGoogleDriveInvalidFile() {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "232142143245325325.doc", "text/plain",
				"test data".getBytes());
		
		String actualResult = filesInDrive.findFileInGoogleDrive(mockMultipartFile);
		
		assertEquals("File not exists in Google Drive 232142143245325325.doc", actualResult);
	}

}
