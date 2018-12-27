package com.example.demo.util;

import java.util.Arrays;

public enum CONTENTTYPE {
	PDF("pdf"), XLSX("xlsx"), XLS("xls"), DOC("doc"), DOCS("docx");

	private String contentType;

	CONTENTTYPE(String contentType) {
		this.contentType = contentType;
	}

	public static boolean getFileType(String contentType) {
		return Arrays.stream(CONTENTTYPE.values()).filter(c -> c.contentType.equals(contentType)).findFirst().isPresent();
	}
}
