package com.example.demo.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileStorageService;
import com.example.dto.FileResponse;
import com.google.gson.Gson;

@RunWith(SpringRunner.class)
@WebMvcTest(value = GoogleDriveFileController.class)
public class GoogleDriveFileControllerTest {

	@MockBean
	private FileStorageService fileStorageService;

	@Inject
	private MockMvc mock;

	@Test
	public void testUploadFileValid() throws Exception {
		FileResponse expected = new FileResponse();
		expected.setFileName("test.docs");

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test.docs", "application/octet-stream",
				"test data".getBytes());

		Mockito.when(fileStorageService.uploadFileInGoogleDrive(Mockito.any(MultipartFile.class))).thenReturn(expected);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/uploadFile").file(mockMultipartFile);
		MvcResult result = mock.perform(requestBuilder).andReturn();

		Gson g = new Gson();
		FileResponse actualResult = g.fromJson(result.getResponse().getContentAsString(), FileResponse.class);

		assertEquals(expected.getFileName(), actualResult.getFileName());
	}

	@Test
	public void testUploadMultipleFiles() throws Exception {
		FileResponse request = null;
		request = new FileResponse();
		request.setFileName("test.xml");

		MockMultipartFile mockMultipartFile = new MockMultipartFile("files", "test.xml", "text/plain",
				"test data".getBytes());
		MockMultipartFile mockMultipartFile1 = new MockMultipartFile("files", "test.doc", "text/plain",
				"test data".getBytes());

		Mockito.when(fileStorageService.uploadFileInGoogleDrive(Mockito.any(MultipartFile.class))).thenReturn(request);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.multipart("/uploadMultipleFiles").file(mockMultipartFile)
				.file(mockMultipartFile1);

		mock.perform(requestBuilder).andReturn();

		verify(fileStorageService, times(2)).uploadFileInGoogleDrive(Mockito.any(MultipartFile.class));
	}

	@Test
	public void testFindFileInGoogleDriveInvalidFile() throws Exception {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", "test.xml", "text/plain",
				"test data".getBytes());
		Mockito.when(fileStorageService.getGoogleFilesByName(Mockito.any(MultipartFile.class)))
				.thenReturn("Sorry! Filename extension invalid ");
		RequestBuilder request = MockMvcRequestBuilders.multipart("/searchFile").file(mockMultipartFile);
		MvcResult result = mock.perform(request).andReturn();

		String expected = "Sorry! Filename extension invalid " + mockMultipartFile.getOriginalFilename();

		assertEquals(expected, result.getResponse().getContentAsString());
	}

	@Test
	public void testFindFileInGoogleDriveValidFile() throws Exception {
		String expected = "File Exists in Google Drive";
		MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", "test.pdf", "application/pdf",
				"test data".getBytes());

		Mockito.when(fileStorageService.getGoogleFilesByName(Mockito.any(MultipartFile.class)))
				.thenReturn("File Exists in Google Drive");

		RequestBuilder request = MockMvcRequestBuilders.multipart("/searchFile").file(mockMultipartFile);
		MvcResult result = mock.perform(request).andReturn();

		assertEquals(expected, result.getResponse().getContentAsString());
	}
}
