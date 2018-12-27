package com.example.demo.dao;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.example.dto.FileResponse;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class FileStorageDaoTest {

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplateMock;
	
	@InjectMocks
	private FileStorageDaoImpl fileStorageDaoImpl;
	
	@Test
	public void testSaveUpdateinRecordDB() {
		Mockito.when(jdbcTemplateMock.update(Mockito.anyString(), Mockito.anyMap())).thenReturn(1);
		int actualResult = fileStorageDaoImpl.save(getFileResponseRequestDate());
		assertEquals(1, actualResult);
	}
	
	@Test
	public void testSaveNotUpdateRecordDB() {
		Mockito.when(jdbcTemplateMock.update(Mockito.anyString(), Mockito.anyMap())).thenReturn(0);
		int actualResult = fileStorageDaoImpl.save(getFileResponseRequestDate());
		assertEquals(0, actualResult);
	}
	
	private static FileResponse getFileResponseRequestDate() {
		FileResponse request = new FileResponse();
		request.setFileName("test.pdf");
		request.setFileType("application/pdf");
		request.setSize(34825);
		request.setCreationDate(new Date());
		return request;
	}
}
