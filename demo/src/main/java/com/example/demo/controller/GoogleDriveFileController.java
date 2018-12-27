package com.example.demo.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileStorageService;
import com.example.demo.util.CONTENTTYPE;
import com.example.dto.FileResponse;

/*
 * Create Credentials for Google Drive API
 * https://o7planning.org/en/11917/create-credentials-for-google-drive-api
 * 
*/
@RestController
public class GoogleDriveFileController {

	@Inject
	private FileStorageService fileStorageService;

	@PostMapping("/uploadFile")
	public FileResponse uploadFile(@RequestParam("file") MultipartFile file) {
		FileResponse response = fileStorageService.uploadFileInGoogleDrive(file);
		return new FileResponse(response.getFileName(), file.getContentType(), file.getSize(),
				response.getCreationDate(), response.getErrorMsg());
	}

	@PostMapping("/uploadMultipleFiles")
	public List<FileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files).stream().map(file -> uploadFile(file)).collect(Collectors.toList());
	}

	@PostMapping(value = "/searchFile")
	public String findFileInGoogleDrive(@RequestPart("fileName") MultipartFile fileName) throws IOException {
		String fileContentType = fileName.getOriginalFilename()
				.substring(fileName.getOriginalFilename().lastIndexOf('.') + 1);

		if (!CONTENTTYPE.getFileType(fileContentType)) {
			return "Sorry! Filename extension invalid " + fileName.getOriginalFilename();
		}
		return fileStorageService.getGoogleFilesByName(fileName);
	}

}
