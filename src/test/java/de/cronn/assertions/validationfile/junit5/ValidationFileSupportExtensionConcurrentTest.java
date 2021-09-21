package de.cronn.assertions.validationfile.junit5;

import static de.cronn.assertions.validationfile.junit5.ValidationFileSupport.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
@WithValidationFileSupport
public class ValidationFileSupportExtensionConcurrentTest {
	@Test
	void testOne() {
		assertThat(validationFileAssertions().getTestName()).isEqualTo("ValidationFileSupportExtensionConcurrentTest_testOne");
	}

	@Test
	void testTwo() {
		assertThat(validationFileAssertions().getTestName()).isEqualTo("ValidationFileSupportExtensionConcurrentTest_testTwo");
	}

	@Test
	void testThree() {
		assertThat(validationFileAssertions().getTestName()).isEqualTo("ValidationFileSupportExtensionConcurrentTest_testThree");
	}

}
