package com.example.demo.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.example.demo.drive.FilesInGoogleDrive;
import com.example.dto.FileResponse;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FileStorageServiceTest {

	@Mock
	private FilesInGoogleDrive filesInDriveMock;

	@InjectMocks
	private FileStorageServiceImpl fileStorageService;

	@Test
	public void testUploadFileInGoogleDrive() {
		FileResponse expected = new FileResponse();
		expected.setFileName("test.docs");
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.doc", "text/plain",
				"test data".getBytes());
		Mockito.when(filesInDriveMock.createFileInGoogleDrive(Mockito.any())).thenReturn(expected);
		FileResponse actualResult = fileStorageService.uploadFileInGoogleDrive(mockMultipartFile);
		assertEquals(expected.getFileName(), actualResult.getFileName());
	}
	
	@Test
	public void testGetGoogleFilesByName() {
		String expected = "File Exists in Google Drive";
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.pdf", "text/plain",
				"test data".getBytes());
		Mockito.when(filesInDriveMock.findFileInGoogleDrive(Mockito.any())).thenReturn("File Exists in Google Drive");
		String actual = fileStorageService.getGoogleFilesByName(mockMultipartFile);
		assertEquals(expected, actual);
	}
}
