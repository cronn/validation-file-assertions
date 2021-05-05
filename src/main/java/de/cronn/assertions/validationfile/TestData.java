package de.cronn.assertions.validationfile;

import java.nio.file.Path;
import java.nio.file.Paths;

public final class TestData {
	private static final Path TEST_DATA_DIR = Paths.get("data", "test");
	public static final Path TEST_TEMPORARY_DATA_DIR = TEST_DATA_DIR.resolve(TestDataDir.TMP.getDir());
	public static final Path TEST_VALIDATION_DATA_DIR = TEST_DATA_DIR.resolve(TestDataDir.VALIDATION.getDir());
	public static final Path TEST_OUTPUT_DATA_DIR = TEST_DATA_DIR.resolve(TestDataDir.OUTPUT.getDir());

	private TestData() {
	}

	public enum TestDataDir {
		TMP("tmp"),
		VALIDATION("validation"),
		OUTPUT("output");

		private final String dir;

		TestDataDir(String dir) {
			this.dir = dir;
		}

		public String getDir() {
			return dir;
		}

	}

	public static Path validationFilePath(String fileName) {
		assertFilenameHasText(fileName);
		return TestData.TEST_VALIDATION_DATA_DIR.resolve(fileName);
	}

	public static Path outputPath(String fileName) {
		assertFilenameHasText(fileName);
		return TestData.TEST_OUTPUT_DATA_DIR.resolve(fileName);
	}

	public static Path tmpPath(String fileName) {
		assertFilenameHasText(fileName);
		return TestData.TEST_TEMPORARY_DATA_DIR.resolve(fileName);
	}

	private static void assertFilenameHasText(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			throw new IllegalArgumentException("filename must not be blank");
		}
	}

}
