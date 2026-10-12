package com.tushar.supportportal.File;

import com.tushar.supportportal.File.interfaces.FileValidator;
import com.tushar.supportportal.File.props.FileUploadProperties;
import com.tushar.supportportal.domain.HttpResponse;
import com.tushar.supportportal.exception.custom.FileValidationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public class FileTypeValidator implements FileValidator {

	private final FileUploadProperties props;

	public FileTypeValidator(FileUploadProperties props) {
		this.props = props;
	}

	@Override
	public boolean validate(MultipartFile file, List<HttpResponse> httpResponse) {
		if (file.isEmpty()) {
			throw new FileValidationException("Uploaded file is empty", List.of());
		}
		if (file.getSize() > props.getMaxFileSize()) {
			throw new FileValidationException(String.format("File too large: %d bytes (max %d bytes)", file.getSize(), props.getMaxFileSize()), httpResponse);
		}
//		System.out.println(file.getOriginalFilename() + " " + file.getContentType());

		// Check extension
		String ext = StringUtils.getFilenameExtension(file.getOriginalFilename());
		if (ext == null || !props.getAllowedExtensions().contains(ext.toLowerCase())) {
			throw new FileValidationException("Invalid file extension: ." + ext, httpResponse);
		}

		if (!props.getAllowedMimeTypes().contains(file.getContentType())) {
			throw new FileValidationException("Invalid MIME type: " + file.getContentType(), httpResponse);
		}
		return true;

	}
}
