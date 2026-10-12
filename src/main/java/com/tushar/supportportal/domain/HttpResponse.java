package com.tushar.supportportal.domain;

import org.springframework.http.HttpStatus;

import java.util.List;

public record HttpResponse(
		int statusCode,
		String reason,
		String body,
		HttpStatus httpStatus,
		List<FileUploadResponse> fileUploadResponseList
) {
	public record FileUploadResponse(String storedFilename, String extension, String contentType, long size) {
	}
}
