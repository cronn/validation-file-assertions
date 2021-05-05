package de.cronn.assertions.validationfile;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FileBasedComparisonFailureTest {

	@Test
	void testMessage_givenAllParameters() {
		FileBasedComparisonFailure failure = new FileBasedComparisonFailure("expected", "actual", "filenameExpected", "filenameActual");

		assertThat(failure.isExpectedDefined()).isTrue();
		assertThat(failure.getExpected().getValue()).isEqualTo("expected");
		assertThat(failure.isActualDefined()).isTrue();
		assertThat(failure.getActual().getValue()).isEqualTo("actual");
		assertThat(failure.getMessage()).isEqualTo("\n" +
			"--- expected/filenameExpected\n" +
			"+++ actual/filenameActual\n" +
			"@@ -1,1 +1,1 @@\n" +
			"-expected\n" +
			"+actual");
	}

	@Test
	void testMessage_expectedAndActualEquals_thenExceptionThrownOnConstruction() {
		assertThatCode(() -> new FileBasedComparisonFailure("expected", "expected", null, null))
			.hasMessage("expected and actual values are equal");
	}

	@Test
	void testMessage_nullGivenAsExpected() {
		FileBasedComparisonFailure failure = new FileBasedComparisonFailure(null, "actual", null, null);

		assertThat(failure.getMessage()).isEqualTo("\n" +
			"--- expected\n" +
			"+++ actual\n" +
			"@@ -1,0 +1,1 @@\n" +
			"+actual");
	}

	@Test
	void testMessage_nullGivenAsActual() {
		FileBasedComparisonFailure failure = new FileBasedComparisonFailure("expected", null, null, null);

		assertThat(failure.getMessage()).isEqualTo("\n" +
			"--- expected\n" +
			"+++ actual\n" +
			"@@ -1,1 +1,0 @@\n" +
			"-expected");
	}

	@Test
	void testMessage_nullGivenAsFileNameActual() {
		FileBasedComparisonFailure failure = new FileBasedComparisonFailure("expected", "actual", "filenameExpected", null);

		assertThat(failure.getMessage()).isEqualTo("\n" +
			"--- expected/filenameExpected\n" +
			"+++ actual\n" +
			"@@ -1,1 +1,1 @@\n" +
			"-expected\n" +
			"+actual");
	}

	@Test
	void testMessage_nullGivenAsFileNameExpected() {
		FileBasedComparisonFailure failure = new FileBasedComparisonFailure("expected", "actual", null, "filenameActual");

		assertThat(failure.getMessage()).isEqualTo("\n" +
			"--- expected\n" +
			"+++ actual/filenameActual\n" +
			"@@ -1,1 +1,1 @@\n" +
			"-expected\n" +
			"+actual");
	}

	@Test
	void testMessage_nullGivenAsBothFileNames() {
		FileBasedComparisonFailure failure = new FileBasedComparisonFailure("expected", "actual", null, null);

		assertThat(failure.getMessage()).isEqualTo("\n" +
			"--- expected\n" +
			"+++ actual\n" +
			"@@ -1,1 +1,1 @@\n" +
			"-expected\n" +
			"+actual");
	}

	@Test
	void testMessage_shouldGenerateMultilineDiff() {
		FileBasedComparisonFailure failure = new FileBasedComparisonFailure("line1\nline2", "line1\nline2\nline3", null, null);

		assertThat(failure.getMessage()).isEqualTo("\n" +
			"--- expected\n" +
			"+++ actual\n" +
			"@@ -1,2 +1,3 @@\n" +
			" line1\n" +
			" line2\n" +
			"+line3");
	}

}
