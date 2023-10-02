package de.cronn.configurationtest;

import de.cronn.assertions.validationfile.TestData;

import de.cronn.assertions.validationfile.junit5.JUnit5ValidationFileAssertions;

import org.junit.jupiter.api.Test;

public class ConfigurablePathTest implements JUnit5ValidationFileAssertions {

	@Test
	void testPath_withImplementedConfiguration() {
		assertWithFile(TestData.TEST_OUTPUT_DATA_DIR.toString());
	}

}
