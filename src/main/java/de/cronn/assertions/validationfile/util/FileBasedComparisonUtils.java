package de.cronn.assertions.validationfile.util;

import static de.cronn.assertions.validationfile.normalization.StringNormalizer.*;
import static java.nio.file.StandardOpenOption.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.regex.Pattern;

import de.cronn.assertions.validationfile.FileBasedComparisonFailure;
import de.cronn.assertions.validationfile.TestData;
import de.cronn.assertions.validationfile.exception.RuntimeIOException;
import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public final class FileBasedComparisonUtils {

	private static final Charset CHARSET = StandardCharsets.UTF_8;
	private static final String NEW_FILE_HEADER_PREFIX = "=== new file \"";
	private static final String NEW_FILE_HEADER_SUFFIX = "\" ===\n";
	private static final Pattern ILLEGAL_WINDOWS_FILE_NAME_CHARS = Pattern.compile("[\u0000-\u001f<>:\"|?*\\\\]");

	private FileBasedComparisonUtils() {
	}

	/**
	 * do not use this directly, rather use ValidationFileAssertions#compareActualWithFile(java.lang.String, de.cronn.assertions.validationfile.normalization.ValidationNormalizer)
	 */
	public static void compareActualWithFileHidden(String actualOutput, String filename, ValidationNormalizer normalizer) {
		String fixedFilename = validateAndFixFilename(filename);
		String fileNameRawFile = fixedFilename + ".raw";
		writeTmp(actualOutput, fileNameRawFile);
		String normalizedOutput = normalizer != null ? normalizer.normalize(actualOutput) : actualOutput;
		String normalizedActual = normalizeLineEndings(normalizedOutput);
		prefillIfNecessary(fixedFilename, normalizedActual);
		String expected = readValidationFile(fixedFilename);
		writeOutput(normalizedActual, fixedFilename);
		assertEquals(expected, normalizedActual, fixedFilename, fixedFilename);
	}

	public static void assertValidationFilesAreEqual(String filename1, String filename2) {
		String file1Content = readValidationFile(filename1);
		String file2Content = readValidationFile(filename2);
		assertEquals(file2Content, file1Content, filename1, filename2);
	}

	public static void compareFileDiffStyle(String expected, String actual) {
		assertEquals(expected, actual, null, null);
	}

	public static void assertEquals(String expected, String actual, String filenameExpected, String filenameActual) {
		if (!Objects.equals(expected, actual)) {
			throw new FileBasedComparisonFailure(expected, actual, filenameExpected, filenameActual);
		}
	}

	public static String validateAndFixFilename(String filename) {
		if (filename.endsWith(".txt.txt")) {
			throw new IllegalArgumentException("Illegal filename: '" + filename + "'");
		}
		return ILLEGAL_WINDOWS_FILE_NAME_CHARS.matcher(filename).replaceAll("_");
	}

	public static String readValidationFile(String fileName) {
		return read(TestData.validationFilePath(fileName));
	}

	private static String read(Path validation) {
		try {
			return normalizeLineEndings(new String(Files.readAllBytes(validation), CHARSET));
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

	public static boolean prefillIfNecessary(String fileName, String normalizedActual) {
		Path validationFile = TestData.validationFilePath(fileName);
		if (!validationFile.toFile().exists() && normalizedActual != null) {
			String header = NEW_FILE_HEADER_PREFIX + validationFile + NEW_FILE_HEADER_SUFFIX;
			write(header + normalizedActual, validationFile);
			return true;
		}
		return false;
	}

	public static void writeOutput(String actual, String fileName) {
		write(actual, TestData.outputPath(fileName));
	}

	public static void writeTmp(String content, String fileName) {
		writeTmp(content.getBytes(CHARSET), fileName);
	}

	public static void writeTmp(byte[] content, String fileName) {
		write(content, TestData.tmpPath(fileName));
	}

	public static void write(String actual, Path path) {
		write(actual.getBytes(CHARSET), path);
	}

	public static void write(byte[] actual, Path path) {
		try {
			Files.createDirectories(path.getParent());
			Files.write(path, actual, TRUNCATE_EXISTING, WRITE, CREATE);
		} catch (IOException e) {
			throw new RuntimeIOException(e);
		}
	}

}
