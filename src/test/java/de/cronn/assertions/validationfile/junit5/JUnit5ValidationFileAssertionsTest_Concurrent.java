package de.cronn.assertions.validationfile.junit5;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Execution(ExecutionMode.CONCURRENT)
class JUnit5ValidationFileAssertionsTest_Concurrent implements JUnit5ValidationFileAssertions {

	@Test
	void testOne() {
		assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_Concurrent_testOne");
	}

	@Test
	void testTwo() {
		assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_Concurrent_testTwo");
	}

	@Test
	void testThree() {
		assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_Concurrent_testThree");
	}
}
