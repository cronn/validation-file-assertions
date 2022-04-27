package de.cronn.assertions.validationfile.extension;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import de.cronn.assertions.validationfile.DirsConfig;

public class ValidationFilesTestHelper extends CleanupValidationFilesAfterAllTests {

	private static final Charset CHARSET = StandardCharsets.UTF_8;

	public void copyOutputToValidation(String validationFileName) throws IOException {
		Path source = DirsConfig.DEFAULT.outputPath(validationFileName);
		Path target = DirsConfig.DEFAULT.validationFilePath(validationFileName);
		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
	}

	public List<String> linesDiffOutputValidation(String validationFileName) throws IOException {
		return linesDiff(DirsConfig.DEFAULT.outputPath(validationFileName), DirsConfig.DEFAULT.validationFilePath(validationFileName));
	}

	public List<String> linesDiff(Path path1, Path path2) throws IOException {
		List<String> content1 = readLines(path1);
		List<String> content2 = readLines(path2);

		List<String> diff = new ArrayList<>();
		for (String line : content1) {
			if (!content2.contains(line)) {
				diff.add("+" + line);
			}
		}
		for (String line : content2) {
			if (!content1.contains(line)) {
				diff.add("-" + line);
			}
		}

		return diff;
	}

	private List<String> readLines(Path path) throws IOException {
		return Files.readAllLines(path, CHARSET);
	}
}
