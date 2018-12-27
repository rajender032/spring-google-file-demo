package com.example.demo.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.drive.FilesInGoogleDrive;
import com.example.dto.FileResponse;

@Service
public class FileStorageServiceImpl implements FileStorageService {

	@Inject
	private FilesInGoogleDrive findFile;

	@Override
	public FileResponse uploadFileInGoogleDrive(MultipartFile file) {
		return findFile.createFileInGoogleDrive(file);
	}

	@Override
	public String getGoogleFilesByName(MultipartFile fileName) {
		return findFile.findFileInGoogleDrive(fileName);
	}

}
