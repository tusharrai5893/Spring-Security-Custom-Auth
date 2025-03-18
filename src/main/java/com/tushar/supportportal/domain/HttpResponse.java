package com.tushar.supportportal.domain;

import org.springframework.http.HttpStatus;

public class HttpResponse {
	private int statusCode;
	private String reason;
	private String message;
	private HttpStatus httpStatus;

	public HttpResponse() {
	}

	public HttpResponse(int statusCode, String reason, String message, HttpStatus httpStatus) {
		this.statusCode = statusCode;
		this.reason = reason;
		this.message = message;
		this.httpStatus = httpStatus;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
}