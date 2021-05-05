package de.cronn.assertions.validationfile.junit5;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class JUnit5ValidationFileAssertionsTest implements JUnit5ValidationFileAssertions {

	static List<String> testNamesBefore = new ArrayList<>();
	static List<String> testNamesAfter = new ArrayList<>();

	@BeforeEach
	void setUp() {
		testNamesBefore.add(getTestName());
	}

	@AfterEach
	void tearDown() {
		testNamesAfter.add(getTestName());
	}

	@AfterAll
	static void afterAll() {
		assertThat(testNamesBefore).containsExactlyInAnyOrder(
			"JUnit5ValidationFileAssertionsTest_otherTest",
			"JUnit5ValidationFileAssertionsTest_myTest",
			"JUnit5ValidationFileAssertionsTest_dynamicTests",
			"JUnit5ValidationFileAssertionsTest_NestedTestWithExplicitTraits_nestedTestExplicit",
			"JUnit5ValidationFileAssertionsTest_NestedTest_nestedTest",
			"JUnit5ValidationFileAssertionsTest_NestedTest_NestedNestedTest_nestedNestedTest"
		);
		assertThat(testNamesAfter).containsExactlyInAnyOrder(
			"JUnit5ValidationFileAssertionsTest_otherTest",
			"JUnit5ValidationFileAssertionsTest_myTest",
			"JUnit5ValidationFileAssertionsTest_dynamicTests",
			"JUnit5ValidationFileAssertionsTest_NestedTestWithExplicitTraits_nestedTestExplicit",
			"JUnit5ValidationFileAssertionsTest_NestedTest_nestedTest",
			"JUnit5ValidationFileAssertionsTest_NestedTest_NestedNestedTest_nestedNestedTest"
		);
	}

	@Test
	void myTest() {
		assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_myTest");
	}

	@Test
	void otherTest() {
		assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_otherTest");
	}

	@Nested
	class NestedTest {

		@Nested
		class NestedNestedTest {

			@Test
			void nestedNestedTest() {
				assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_NestedTest_NestedNestedTest_nestedNestedTest");
			}
		}

		@Test
		void nestedTest() {
			assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_NestedTest_nestedTest");
		}
	}

	@Nested
	class NestedTestWithExplicitTraits implements JUnit5ValidationFileAssertions {

		@Test
		void nestedTestExplicit() {
			assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_NestedTestWithExplicitTraits_nestedTestExplicit");
		}
	}

	@TestFactory
	Collection<DynamicTest> dynamicTests() {
		return Arrays.asList(
			DynamicTest.dynamicTest("dynamic test", () ->
				assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_dynamicTests")),
			DynamicTest.dynamicTest("other dynamic test", () ->
				assertThat(getTestName()).isEqualTo("JUnit5ValidationFileAssertionsTest_dynamicTests"))
		);
	}

}
