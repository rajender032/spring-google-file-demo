package com.example.demo.service;

import org.springframework.web.multipart.MultipartFile;

import com.example.dto.FileResponse;

public interface FileStorageService {

	FileResponse uploadFileInGoogleDrive(MultipartFile files);

	String getGoogleFilesByName(MultipartFile fileName);

}
