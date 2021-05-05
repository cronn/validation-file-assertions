package de.cronn.assertions.validationfile.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TestNameUtilsTest {

	@Test
	void testByExample_getTestName() {
		assertThat(TestNameUtils.getTestName(TestNameUtilsTest.class, "testName_suffix")).isEqualTo("TestNameUtilsTest_testName_suffix");
		assertThat(TestNameUtils.getTestName(TestNameUtilsTest.class, "_testName_suffix")).isEqualTo("TestNameUtilsTest_testName_suffix");
		assertThat(TestNameUtils.getTestName(Object.class, "testName")).isEqualTo("Object_testName");

		assertThat(TestNameUtils.getTestName(NestedClass.class, "testName")).isEqualTo("TestNameUtilsTest_NestedClass_testName");
		assertThat(TestNameUtils.getTestName(NestedClass.NestedNestedClass.class, "testName")).isEqualTo("TestNameUtilsTest_NestedClass_NestedNestedClass_testName");
	}

	class NestedClass {
		class NestedNestedClass {
		}
	}
}
