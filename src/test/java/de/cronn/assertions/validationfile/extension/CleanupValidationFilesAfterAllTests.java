package de.cronn.assertions.validationfile.extension;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import de.cronn.assertions.validationfile.TestData;

public class CleanupValidationFilesAfterAllTests implements BeforeAllCallback, AfterAllCallback {

	private Set<Path> filesBefore;

	@Override
	public void beforeAll(ExtensionContext context) throws IOException {
		filesBefore = collectAllFiles();
	}

	@Override
	public void afterAll(ExtensionContext context) throws IOException {
		cleanupFiles();
	}

	public void cleanupFiles() throws IOException {
		Set<Path> filesToRemove = collectAddedFiles();
		for (Path file : filesToRemove) {
			Files.deleteIfExists(file);
		}
	}

	Set<Path> getFilesBeforeClass() {
		return filesBefore;
	}

	public Set<Path> collectAddedFiles() throws IOException {
		Set<Path> allFiles = collectAllFiles();
		allFiles.removeAll(getFilesBeforeClass());
		return allFiles;
	}

	Set<Path> collectAllFiles() throws IOException {
		Set<Path> files = new LinkedHashSet<>();
		files.addAll(listFiles(TestData.TEST_VALIDATION_DATA_DIR));
		files.addAll(listFiles(TestData.TEST_OUTPUT_DATA_DIR));
		files.addAll(listFiles(TestData.TEST_TEMPORARY_DATA_DIR));
		return files;
	}

	private Set<Path> listFiles(Path dir) throws IOException {
		if (!Files.exists(dir)) {
			return Collections.emptySet();
		}
		assertThat(Files.isDirectory(dir)).as("Expected directory, but was: %s", dir).isTrue();
		try (Stream<Path> paths = Files.walk(dir)) {
			return paths.filter(Files::isRegularFile)
				.collect(Collectors.toCollection(LinkedHashSet::new));
		}
	}
}

