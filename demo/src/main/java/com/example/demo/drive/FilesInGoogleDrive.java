package com.example.demo.drive;

import java.io.IOException;
import java.util.Date;

import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dao.FileStorageDao;
import com.example.demo.exception.FileStorageException;
import com.example.demo.util.CONTENTTYPE;
import com.example.demo.util.GoogleDriveUtils;
import com.example.dto.FileResponse;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

@Component
public class FilesInGoogleDrive {

	@Inject
	private FileStorageDao fileStorageDao;

	public FileResponse createFileInGoogleDrive(MultipartFile file) {
		FileResponse fileResponse = null;
		String fileName = file.getOriginalFilename();
		String fileContentType = fileName.substring(fileName.lastIndexOf('.') + 1);
		if (CONTENTTYPE.getFileType(fileContentType)) {
			try {
				Drive driveService = GoogleDriveUtils.getDriveService();
				String contentType = file.getContentType();
				byte[] uploadData = file.getBytes();

				AbstractInputStreamContent uploadStreamContent = new ByteArrayContent(contentType, uploadData);

				File fileMetadata = new File();
				fileMetadata.setName(fileName);

				File driveFile = driveService.files().create(fileMetadata, uploadStreamContent)
						.setFields("id, webContentLink, webViewLink, parents").execute();
				if (!driveFile.isEmpty()) {
					fileResponse = uploadFile(file, new Date());
					fileStorageDao.save(fileResponse);
				}
			} catch (IOException e) {
				throw new FileStorageException("Could not store file " + fileName + " In Google Drive. Please try again!", e);
			}
		} else {
			fileResponse = new FileResponse();
			fileResponse.setFileName(fileName);
			fileResponse.setErrorMsg("Sorry! Filename extension invalid sequence " + fileName);
		}

		return fileResponse;
	}

	public String findFileInGoogleDrive(MultipartFile file) {

		String fileName = file.getOriginalFilename();
		String query = "name contains '" + fileName + "' and mimeType !='application/vnd.google-apps.folder'";

		FileList files;
		try {
			Drive driveService = GoogleDriveUtils.getDriveService();

			files = driveService.files().list().setQ(query).setSpaces("drive")
					.setFields("nextPageToken, files(id, name, createdTime, mimeType)").execute();

			if (files.getFiles().size() == 0) {
				return "File not exists in Google Drive " + fileName;
			}
			files.getFiles().forEach(Orginalfile -> {
				fileStorageDao.save(uploadFile(file, new Date(Orginalfile.getCreatedTime().getValue())));
			});

			return "File Exists in Google Drive " + fileName;
		} catch (IOException e) {
			throw new FileStorageException("Could not store file " + fileName + ". Please try again!", e);
		}
	}

	private FileResponse uploadFile(MultipartFile file, Date createdDateTime) {
		FileResponse fileResponse = new FileResponse();
		fileResponse.setFileName(file.getOriginalFilename());
		fileResponse.setSize(file.getSize());
		String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
		fileResponse.setFileType(fileExtension);
		fileResponse.setCreationDate(createdDateTime);
		return fileResponse;
	}

}
