package de.cronn.assertions.validationfile;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.ServiceLoader;

import de.cronn.assertions.validationfile.config.Configuration;

public final class TestData {
	private static final Path TEST_DATA_DIR;
	public static final Path TEST_TEMPORARY_DATA_DIR;
	public static final Path TEST_VALIDATION_DATA_DIR;
	public static final Path TEST_OUTPUT_DATA_DIR;

	static {
		Path path = getPath();
		if (path != null) {
			TEST_DATA_DIR = path;
		} else {
			TEST_DATA_DIR = Paths.get("data", "test");
		}
		TEST_TEMPORARY_DATA_DIR = TEST_DATA_DIR.resolve(TestDataDir.TMP.getDir());
		TEST_VALIDATION_DATA_DIR = TEST_DATA_DIR.resolve(TestDataDir.VALIDATION.getDir());
		TEST_OUTPUT_DATA_DIR = TEST_DATA_DIR.resolve(TestDataDir.OUTPUT.getDir());
	}

	private static Path getPath() {
		ServiceLoader<Configuration> serviceLoader = ServiceLoader.load(Configuration.class);
		Iterator<Configuration> iterator = serviceLoader.iterator();
		if (iterator.hasNext()) {
			Configuration loader = iterator.next();
			if (iterator.hasNext()) {
				throw new IllegalArgumentException("More than one validation files configuration found.");
			}
			return loader.getDataDirectory();
		} else {
			return null;
		}
	}

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
