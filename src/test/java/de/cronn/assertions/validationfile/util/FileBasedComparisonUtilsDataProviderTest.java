package de.cronn.assertions.validationfile.util;


import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import de.cronn.assertions.validationfile.ValidationFileAssertions;

class FileBasedComparisonUtilsDataProviderTest implements ValidationFileAssertions {

	private TestInfo testInfo;

	@BeforeEach
	void init(TestInfo testInfo) {
		this.testInfo = testInfo;
	}

	private static Stream<Arguments> objectDataProvider() {
		return Stream.of(
			arguments("one!", "2016-01-01T00:00:00.123456Z"),
			arguments("two+", "2016-01-01")
		);
	}

	@ParameterizedTest
	@ValueSource(strings = { "one!", "t \nw*o+" })
	void shouldCheckAgainstValidationFile(String input) throws Exception {
		assertWithFileWithSuffix(
			input,
			input
		);
	}

	@ParameterizedTest
	@MethodSource("objectDataProvider")
	void shouldCheckAgainstValidationFile(String input, Object secondValue) throws Exception {
		assertWithFileWithSuffix(
			String.format("%sX%s", input, secondValue),
			input + "_" + secondValue
		);
	}

	@Override
	public String getTestName() {
		return TestNameUtils.getTestName(
			getClass(),
			testInfo.getTestMethod()
				.orElseThrow(IllegalStateException::new)
				.getName()
		);
	}

}
