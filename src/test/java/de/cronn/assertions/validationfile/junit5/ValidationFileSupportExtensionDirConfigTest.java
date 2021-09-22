package de.cronn.assertions.validationfile.junit5;

import static de.cronn.assertions.validationfile.junit5.ValidationFileSupport.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.FileBasedComparisonFailure;

@WithValidationFileSupport(base = "build/other-config-dir")
class ValidationFileSupportExtensionDirConfigTest {
	@Test
	void myTest() {
		assertThatThrownBy(() -> validationFileAssertions().assertWithFile("FOOBAR"))
			.isInstanceOf(FileBasedComparisonFailure.class)
			.hasMessageContaining("=== new file \"build/other-config-dir/validation/ValidationFileSupportExtensionDirConfigTest_myTest.txt\" ===");
	}
}
