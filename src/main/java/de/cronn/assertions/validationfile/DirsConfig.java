package de.cronn.assertions.validationfile;

import java.nio.file.Path;
import java.nio.file.Paths;

public interface DirsConfig {
	Path base();

	default Path relative() {
		return Paths.get("");
	}

	default Path validation() {
		return base().resolve("validation").resolve(relative());
	}

	default Path output() {
		return base().resolve("output").resolve(relative());
	}

	default Path tmp() {
		return base().resolve("tmp").resolve(relative());
	}

	default Path validationFilePath(String fileName) {
		assertFilenameHasText(fileName);
		return validation().resolve(fileName);
	}

	default Path outputPath(String fileName) {
		assertFilenameHasText(fileName);
		return output().resolve(fileName);
	}

	default Path tmpPath(String fileName) {
		assertFilenameHasText(fileName);
		return tmp().resolve(fileName);
	}

	static void assertFilenameHasText(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			throw new IllegalArgumentException("filename must not be blank");
		}
	}

	DirsConfig DEFAULT = () -> Paths.get("data", "test");
}
