package com.tushar.supportportal.resource;

import com.tushar.supportportal.File.interfaces.FileValidator;
import com.tushar.supportportal.domain.HttpResponse;
import com.tushar.supportportal.exception.custom.FileValidationException;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@RestController
@RequestMapping
public class Home {

	private final FileValidator validator;
	Logger logger = LoggerFactory.getLogger(Home.class);
	List<HttpResponse> responses = new ArrayList<>();
	private Path uploadDir = Paths.get("./uploads");

	public Home(FileValidator validator) {
		this.validator = validator;
	}

	@PostConstruct
	public void init() throws IOException {
		this.uploadDir = Paths.get(uploadDir.toUri()).toAbsolutePath().normalize();
		try {
			Files.createDirectories(this.uploadDir); // Create directories if they don't exist
		} catch (IOException e) {
			throw new RuntimeException("Could not initialize upload directory!", e);
		}
	}

	@PreDestroy
	public void destroy() {
		try (Stream<Path> paths = Files.walk(this.uploadDir)) {
			// Walk the directory tree
			// Sort in reverse order so files are deleted before directories
			paths.sorted(Comparator.reverseOrder())
					// Skip the root upload directory itself for this operation (delete contents only)
					.filter(path -> !path.equals(uploadDir)).forEach(path -> {
						try {
							if (Files.exists(path)) {
								Files.delete(path);
							}
						} catch (IOException e) {
							// Log error but continue trying to delete other files/dirs
						}
					});
		} catch (IOException e) {
			// Do not typically re-throw exceptions here, as it might interfere with the shutdown process.
		}
	}

	@PostMapping("/upload")
	public ResponseEntity<?> store(@RequestPart("filen") MultipartFile file) {
		System.out.println(file.getOriginalFilename());
		if (validator.validate(file, null)) {
			saveFile(file);
		}
		System.out.println("✅ Accepted file: " + file.getOriginalFilename());
		return ResponseEntity.ok("File uploaded successfully");
	}

	private void saveFile(MultipartFile file) {
		String[] fileExtension = new String[0];
		try {
			String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
			fileExtension = new String[]{ originalFilename.split("\\.")[0], originalFilename.split("\\.")[1] };
			Path targetLocation = this.uploadDir.resolve(fileExtension[0]);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			logger.info("Successfully processed and stored file {} as {}", originalFilename, targetLocation);
			if (responses.isEmpty()) {
				List<HttpResponse.FileUploadResponse> fileUploadList = new ArrayList<>();
				HttpResponse httpResponse = new HttpResponse(HttpStatus.OK.value(), "Success", "File upload initiated", HttpStatus.OK, fileUploadList);
				responses.add(httpResponse);
			}
			String[] finalFileExtension1 = fileExtension;
			responses.forEach(r -> r.fileUploadResponseList()
					.add(( new HttpResponse.FileUploadResponse("File '" + originalFilename + "' uploaded successfully.",
							finalFileExtension1[1], file.getContentType(), file.getSize()) )));
		} catch (IOException e) {
			// Decide how to handle partial failures: stop all, collect errors, etc.
			// Here, we'll add an error response and continue
			String[] finalFileExtension = fileExtension;
			responses.stream().parallel().forEach(r ->
					r.fileUploadResponseList().add(
							new HttpResponse.FileUploadResponse("Failed to upload file '" + file.getOriginalFilename() + "'. Error: " + e.getMessage(), finalFileExtension[1], file.getContentType(), file.getSize())));
			// Optionally, return an error status immediately or collect all errors
			// For simplicity, we collect results here. Consider using HttpStatus.MULTI_STATUS (207) if results are mixed.
			if (!responses.isEmpty()) {
				throw new FileValidationException(e.getLocalizedMessage(), responses);
			}
		}
	}

	@PostMapping("/upload/bulk")
	public ResponseEntity<?> bulk(@RequestPart("file") List<MultipartFile> files) {
		// 1) Validate
		files.forEach(file -> {
			if (file.getSize() < 12 && validator.validate(file, responses)) {
				saveFile(file);
			} else {
				throw new MultipartException(Objects.requireNonNull(file.getOriginalFilename()));
			}
		});
		// Return results for all files (success or failure)
		// Consider returning 207 Multi-Status if some files failed processing
		boolean allSucceeded = responses.stream().flatMap(r -> r.fileUploadResponseList().stream()).allMatch(resp -> Objects.isNull(resp.storedFilename()));
		HttpStatus status = allSucceeded ? HttpStatus.CREATED : HttpStatus.MULTI_STATUS;
		return ResponseEntity.status(status).body(responses);
	}

	@GetMapping("/")
	public ResponseEntity<String> users() {
		return ResponseEntity.ok().body("Home Controller");
	}
}
