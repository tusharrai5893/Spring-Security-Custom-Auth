package com.tushar.supportportal.File.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "file.upload")
public class FileUploadProperties {
	/**
	 * Max file size in bytes.
	 */
	private int maxFileSize;
	/**
	 * Acceptable MIME types, exactly as reported by Apache Tika
	 */
	private List<String> allowedMimeTypes;
	/**
	 * Acceptable file extensions (lower‐case, without leading dot).
	 */
	private List<String> allowedExtensions;

	// getters & setters

	public int getMaxFileSize() {
		return maxFileSize;
	}

	public void setMaxFileSize(int maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	public List<String> getAllowedMimeTypes() {
		return allowedMimeTypes;
	}

	public void setAllowedMimeTypes(List<String> allowedMimeTypes) {
		this.allowedMimeTypes = allowedMimeTypes;
	}

	public List<String> getAllowedExtensions() {
		return allowedExtensions;
	}

	public void setAllowedExtensions(List<String> allowedExtensions) {
		this.allowedExtensions = allowedExtensions;
	}
}
