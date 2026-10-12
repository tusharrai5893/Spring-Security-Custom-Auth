package com.tushar.supportportal;

import com.tushar.supportportal.File.FileTypeValidator;
import com.tushar.supportportal.File.interfaces.FileValidator;
import com.tushar.supportportal.File.props.FileUploadProperties;
import com.tushar.supportportal.exception.custom.FileValidationException;
import com.tushar.supportportal.resource.Home;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SupportPortalTests {

	@Autowired
	private MockMvc mockMvc;
	@Mock
	private Home storageService;
	private MockMultipartFile goodFile;
	private MockMultipartFile badFile;
	private FileValidator validator;
	private FileUploadProperties props;

	@Test
	void upload_validFile_returns200() throws Exception {
		mockMvc.perform(multipart("/upload")
						.file(goodFile)
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isOk())
				.andExpect(content().string("File uploaded successfully"));
	}

	@Test
	void upload_invalidFile_returns400WithJsonError() throws Exception {
		doThrow(new FileValidationException("not allowed", null))
				.when(storageService).store(badFile);

		mockMvc.perform(multipart("/upload")
						.file(badFile)
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(status().isBadRequest())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.code").value("VALIDATION_FAILED"))
				.andExpect(jsonPath("$.message").value("not allowed"));
	}

	@BeforeEach
	void setUp() {
		goodFile = new MockMultipartFile(
				"file", "valid.pdf", "application/pdf", "%PDF-1.4".getBytes());
		badFile = new MockMultipartFile(
				"file", "invalid.txt", "text/plain", "oops".getBytes());

		validator = mock(FileValidator.class);
		storageService = new Home(validator);
		props = new FileUploadProperties();
		props.setMaxFileSize(1024); //
		props.setAllowedExtensions(List.of("pdf"));
		props.setAllowedMimeTypes(List.of("application/pdf"));
		validator = new FileTypeValidator(props);
	}

	@Test
	void store_valid_delegatesAndReturns() {
		MockMultipartFile file = new MockMultipartFile(
				"file", "name.pdf", "application/pdf", new byte[]{ 1, 2, 3 });
		// no exception from validator
		assertDoesNotThrow(() -> storageService.store(file));
		verify(validator, times(1)).validate(file, null);
	}

	@Test
	void store_invalid_throws() {
		MockMultipartFile file = new MockMultipartFile(
				"file", "bad.pdf", "application/pdf", new byte[]{});
		doThrow(new FileValidationException("nope", null))
				.when(validator).validate(file, null);

		FileValidationException ex = assertThrows(
				FileValidationException.class,
				() -> storageService.store(file));
		assertEquals("nope", ex.getMessage());
		verify(validator, times(1)).validate(file, null);
	}

	@Test
	void validate_emptyFile_throws() {
		MockMultipartFile empty = new MockMultipartFile(
				"file", "empty.pdf", "application/pdf", new byte[0]);
		FileValidationException ex = assertThrows(
				FileValidationException.class,
				() -> validator.validate(empty, null));
		assertTrue(ex.getMessage().contains("empty"));
	}

	@Test
	void validate_tooLarge_throws() {
		byte[] big = new byte[props.getMaxFileSize() + 1];
		MockMultipartFile large = new MockMultipartFile(
				"file", "big.pdf", "application/pdf", big);
		FileValidationException ex = assertThrows(
				FileValidationException.class,
				() -> validator.validate(large, null));
		assertTrue(ex.getMessage().toLowerCase().contains("too large"));
	}

	@Test
	void validate_badExtension_throws() {
		// correct content but bad extension
		byte[] pdfMagic = "%PDF-1.0".getBytes();
		MockMultipartFile badExt = new MockMultipartFile(
				"file", "doc.txt", "text/plain", pdfMagic);
		FileValidationException ex = assertThrows(
				FileValidationException.class,
				() -> validator.validate(badExt, null));
		assertTrue(ex.getMessage().contains("Invalid file extension"));
	}

	@Test
	void validate_spoofedMime_throws() {
		// .pdf extension but content not PDF
		byte[] random = "I am not a pdf".getBytes();
		MockMultipartFile spoof = new MockMultipartFile(
				"file", "trick.pdf", "application/pdf", random);
		FileValidationException ex = assertThrows(
				FileValidationException.class,
				() -> validator.validate(spoof, null));
		assertTrue(ex.getMessage().contains("Invalid MIME type"));
	}

	@Test
	void validate_validPdf_passes() {
		// starts with PDF header so Tika will detect it
		byte[] pdfMagic = "%PDF-1.4\n%âãÏÓ\n".getBytes();
		MockMultipartFile good = new MockMultipartFile(
				"file", "sample.pdf", "application/pdf", pdfMagic);

		assertDoesNotThrow(() -> validator.validate(good, null));
	}

}
