package de.cronn.assertions.validationfile.junit5;

import static de.cronn.assertions.validationfile.junit5.ValidationFileSupport.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collection;
import java.util.Collections;

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
		return Collections.singletonList(
			DynamicTest.dynamicTest("dynamic test", () ->
				assertThatThrownBy(() -> validationFileAssertions().getTestName())
					.isInstanceOf(IllegalStateException.class)
					.hasMessageContaining("No assertions for test run. Didn't you forgot to annotate with @WithValidationFileSupport or nested/dynamic testcase")));
	}

}
