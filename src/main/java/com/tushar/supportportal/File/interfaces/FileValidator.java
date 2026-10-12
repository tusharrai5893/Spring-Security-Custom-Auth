package com.tushar.supportportal.File.interfaces;

import com.tushar.supportportal.domain.HttpResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileValidator {
	/**
	 * Validate the given multipart file.
	 *
	 * @throws com.example.fileupload.exception.FileValidationException if invalid.
	 */
	boolean validate(MultipartFile file, List<HttpResponse> httpResponse);
}
