package com.tushar.supportportal.exception.custom;

import com.tushar.supportportal.domain.HttpResponse;

import java.util.List;

public class FileValidationException extends RuntimeException {
	private final List<HttpResponse> httpResponses;

	public FileValidationException(String message, List<HttpResponse> httpResponses) {
		super(message);
		this.httpResponses = httpResponses;
	}

	public List<HttpResponse> getHttpResponses() {
		return httpResponses;
	}
}
