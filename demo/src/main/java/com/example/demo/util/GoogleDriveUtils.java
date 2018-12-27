package com.example.demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

public class GoogleDriveUtils {

	private static final Logger log = LoggerFactory.getLogger(GoogleDriveUtils.class);

	private static final String APPLICATION_NAME = "Google Drive API Java Upload/Search File";

	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

	private static final File CREDENTIALS_FOLDER = new File(System.getProperty("user.home"), "credentials");

	private static final String CLIENT_SECRET_FILE_NAME = "client_secret.json";

	private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

	private static FileDataStoreFactory DATA_STORE_FACTORY;

	private static HttpTransport HTTP_TRANSPORT;

	private static Drive driveSerivce;

	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(CREDENTIALS_FOLDER);
		} catch (GeneralSecurityException | IOException e) {
			log.error("Exception while createing Global instance", e);
			System.exit(1);

		}

	}

	private static Credential getCredential() throws IOException {
		File secretFilePath = new File(CREDENTIALS_FOLDER, CLIENT_SECRET_FILE_NAME);
		if (!secretFilePath.exists()) {
			throw new FileNotFoundException(
					"Please copy " + CLIENT_SECRET_FILE_NAME + " to folder " + CREDENTIALS_FOLDER.getAbsolutePath());
		}

		InputStream in = new FileInputStream(secretFilePath);

		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
				clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();

		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");

		return credential;
	}

	public static Drive getDriveService() throws IOException {
		if (driveSerivce != null) {
			return driveSerivce;
		}
		Credential credential = getCredential();
		driveSerivce = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME)
				.build();
		return driveSerivce;
	}
}
