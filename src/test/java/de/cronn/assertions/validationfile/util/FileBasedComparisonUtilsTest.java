package de.cronn.assertions.validationfile.util;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import de.cronn.assertions.validationfile.FileBasedComparisonFailure;
import de.cronn.assertions.validationfile.extension.ValidationFilesTestHelper;

class FileBasedComparisonUtilsTest {

	@RegisterExtension
	static ValidationFilesTestHelper helper = new ValidationFilesTestHelper();

	@AfterEach
	void tearDown() throws IOException {
		helper.cleanupFiles();
	}

	@Test
	void testRunForTheFirstTime_throwsError() throws Exception {
		// Given
		assertThat(helper.collectAddedFiles()).isEmpty();

		// When
		Throwable error = catchThrowable(
			() -> FileBasedComparisonUtils.compareActualWithFileHidden("text", "file.txt", null)
		);

		// Then
		assertThat(error).hasMessage("\n" +
			"--- expected/file.txt\n" +
			"+++ actual/file.txt\n" +
			"@@ -1,2 +1,1 @@\n" +
			"-=== new file \"data/test/validation/file.txt\" ===\n" +
			" text");
		assertThat(helper.collectAddedFiles()).containsExactlyInAnyOrder(
			Paths.get("data/test/validation/file.txt"),
			Paths.get("data/test/output/file.txt"),
			Paths.get("data/test/tmp/file.txt.raw")
		);
		assertThat(helper.linesDiffOutputValidation("file.txt"))
			.containsExactly("-=== new file \"data/test/validation/file.txt\" ===");
	}

	@Test
	void testActualOutputHasChanged_throwsError() throws Throwable {
		// Given
		createPrefilledValidationFile("text", "file.txt");

		// When
		Throwable error = catchThrowable(
			() -> FileBasedComparisonUtils.compareActualWithFileHidden("text\nextra line", "file.txt", null)
		);

		// Then
		assertThat(error).hasMessage("\n" +
			"--- expected/file.txt\n" +
			"+++ actual/file.txt\n" +
			"@@ -1,1 +1,2 @@\n" +
			" text\n" +
			"+extra line");
		assertThat(helper.linesDiffOutputValidation("file.txt"))
			.containsExactly("+extra line");
	}

	@Test
	void testCopyingOutputFileToValidationPath_fixesError() throws Throwable {
		// Given
		createPrefilledValidationFile("text", "file.txt");
		assertThatThrownBy(
			() -> FileBasedComparisonUtils.compareActualWithFileHidden("text\nextra line", "file.txt", null)
		).isInstanceOf(FileBasedComparisonFailure.class);

		// When
		helper.copyOutputToValidation("file.txt");

		// Then
		assertThatCode(
			() -> FileBasedComparisonUtils.compareActualWithFileHidden("text\nextra line", "file.txt", null)
		).doesNotThrowAnyException();
		assertThat(helper.linesDiffOutputValidation("file.txt")).isEmpty();
	}

	@Test
	void testReplaceCharactersThatAreForbiddenInWindowsFileNames() throws Exception {
		final String someAllowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabzdefghijklmnopqrstuvwxyz0123456789.-_/";
		assertThat(FileBasedComparisonUtils.validateAndFixFilename(someAllowedCharacters)).isEqualTo(someAllowedCharacters);

		assertThat(FileBasedComparisonUtils.validateAndFixFilename("some/path_with_back\\slash.ext"))
			.isEqualTo("some/path_with_back_slash.ext");

		assertThat(FileBasedComparisonUtils.validateAndFixFilename("<>:\"|?*")).isEqualTo("_______");
		assertThat(FileBasedComparisonUtils.validateAndFixFilename("\u0000\u0001\u0002\u0003\u0004\u0005" +
			"\u0006\u0007\u0008\u0009\u000b\u000c\n\u000e\u000f\u0010\u0011\u0012\u0013\u0014\u0015\u0016\u0017" +
			"\u0018\u0019\u001a\u001b\u001c\u001d\u001e\u001f")).isEqualTo("_______________________________");
	}

	private void createPrefilledValidationFile(String actualOutput, String validationFileName) throws IOException {
		try {
			FileBasedComparisonUtils.compareActualWithFileHidden(actualOutput, validationFileName, null);
		} catch (FileBasedComparisonFailure failure) {
			helper.copyOutputToValidation(validationFileName);
		}
	}

}
