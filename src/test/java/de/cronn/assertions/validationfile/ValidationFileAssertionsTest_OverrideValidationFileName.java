package de.cronn.assertions.validationfile;

import static org.assertj.core.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;

import de.cronn.assertions.validationfile.extension.CleanupValidationFilesAfterAllTests;
import de.cronn.assertions.validationfile.util.TestNameUtils;

@ExtendWith(CleanupValidationFilesAfterAllTests.class)
class ValidationFileAssertionsTest_OverrideValidationFileName implements ValidationFileAssertions {

	private TestInfo testInfo;

	@BeforeEach
	void init(TestInfo testInfo) {
		this.testInfo = testInfo;
	}

	@Override
	public String getTestName() {
		return TestNameUtils
			.getTestName(getClass(), testInfo.getTestMethod().orElseThrow(IllegalArgumentException::new).getName());
	}

	@Override
	public String getValidationFileName(String baseName, FileExtension extension) {
		String validationFileName = ValidationFileAssertions.super.getValidationFileName(baseName, extension);
		String directory = getClass().getPackage().getName().replaceAll("\\.", "/") + "/";
		return directory + validationFileName;
	}

	@Test
	void assertValidationFileName() {
		Path file = TestData.TEST_VALIDATION_DATA_DIR.resolve(
			"de/cronn/assertions/validationfile/ValidationFileAssertionsTest_OverrideValidationFileName_assertValidationFileName.txt");

		assertThat(file).doesNotExist();

		catchThrowableOfType(() -> assertWithFile("lorem ipsum"), AssertionError.class);

		assertThat(file).exists();
	}
}
