package com.tushar.supportportal.exception;

import com.tushar.supportportal.domain.HttpResponse;
import com.tushar.supportportal.exception.custom.FileValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.Collections;
import java.util.List;

@RestControllerAdvice
public class SupportPostalGlobalException {

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<?> handleValidation(NullPointerException ex) {
		var body = new HttpResponse(503, ex.getClass().toString(), ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE, null);
		return ResponseEntity.status(body.statusCode()).body(body);
	}

	@ExceptionHandler( { FileValidationException.class })
	public ResponseEntity<?> handleValidation(FileValidationException ex) {
		List<HttpResponse> rs = ex.getHttpResponses();
		if (!rs.isEmpty()) {
			var body = new HttpResponse(400, "VALIDATION_FAILED", ex.getMessage(), HttpStatus.BAD_REQUEST, rs.getFirst().fileUploadResponseList());
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		} else {
			var body = new HttpResponse(400, "VALIDATION_FAILED", ex.getMessage(), HttpStatus.BAD_REQUEST, null);
			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}

	}

	@ExceptionHandler( { MultipartException.class })
	public ResponseEntity<?> handleValidation(MultipartException ex) {
		var body = new HttpResponse(413, "MULTIPART_VALIDATION_FAILED", ex.getMessage(), HttpStatus.PAYLOAD_TOO_LARGE, Collections.emptyList());
		return new ResponseEntity<>(body, HttpStatus.PAYLOAD_TOO_LARGE);
	}
}
