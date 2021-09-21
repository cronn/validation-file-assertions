package de.cronn.assertions.validationfile.junit5;

import static de.cronn.assertions.validationfile.junit5.ValidationFileSupport.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

@WithValidationFileSupport
class ValidationFileSupportExtensionTest {
	@Test
	void myTest() {
		assertThat(validationFileAssertions().getTestName()).isEqualTo("ValidationFileSupportExtensionTest_myTest");
	}

	@Test
	void otherTest() {
		assertThat(validationFileAssertions().getTestName()).isEqualTo("ValidationFileSupportExtensionTest_otherTest");
	}

	@TestFactory
	Collection<DynamicTest> dynamicTests() {
		return Arrays.asList(
			DynamicTest.dynamicTest("dynamic test", () ->
				assertThat(validationFileAssertions().getTestName()).isNull()),
			DynamicTest.dynamicTest("other dynamic test", () ->
				assertThat(validationFileAssertions().getTestName()).isNull())
		);
	}

}
