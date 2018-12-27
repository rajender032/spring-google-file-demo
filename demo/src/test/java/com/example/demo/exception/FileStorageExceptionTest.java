package com.example.demo.exception;

import org.junit.Test;

public class FileStorageExceptionTest {

	
	@Test
	public void testFileStorageExceptionString() {
		new FileStorageException("Exception Message");
	}

	@Test
	public void testFileStorageExceptionStringThrowable() {
		new FileStorageException("Exception Message", new Throwable());
	}

}
