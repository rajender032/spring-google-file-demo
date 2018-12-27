package com.example.dto;

import java.util.Date;

public class FileResponse {

	private String fileName;
	private String fileType;
	private long size;
	private Date creationDate;
	private String errorMsg;

	public FileResponse() {

	}

	public FileResponse(String fileName, String fileType, long size, Date creationDate, String errorMsg) {
		this.fileName = fileName;
		this.fileType = fileType;
		this.size = size;
		this.creationDate = creationDate;
		this.errorMsg = errorMsg;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
