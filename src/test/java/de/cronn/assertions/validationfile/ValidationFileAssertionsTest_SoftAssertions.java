package de.cronn.assertions.validationfile;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.cronn.assertions.validationfile.extension.CleanupValidationFilesAfterAllTests;

@ExtendWith(CleanupValidationFilesAfterAllTests.class)
class ValidationFileAssertionsTest_SoftAssertions {

	SoftAssertions softly = new SoftAssertions();

	@Nested
	class FailedAssertionHandlerProvided implements ValidationFileAssertions {

		@Override
		public String getTestName() {
			return "any-test-name";
		}

		@Override
		public FailedAssertionHandler failedAssertionHandler() {
			return callable -> softly.check(callable::call);
		}

		@Test
		void testCompareTwoActualsWithFilesAndSoftlyFailAfter() {
			assertThatThrownBy(() -> {
				assertWithFile("lorem ipsum");
				assertWithFile("another");
				softly.fail("and one normal failure after");
				assertWithFile("not reached without soft assertions");
				softly.assertAll();
			}).hasMessageContainingAll(
				"lorem ipsum",
				"another",
				"and one normal failure after",
				"not reached without soft assertions"
			);
		}
	}

	@Nested
	class NoFailedAssertionHandler implements ValidationFileAssertions {

		@Override
		public String getTestName() {
			return "any-test-name";
		}

		@Test
		void testCompareTwoActualsWithFilesAndSoftlyFailAfter() {
			assertThatThrownBy(() -> {
				assertWithFile("lorem ipsum");
				assertWithFile("another");
				softly.fail("and one normal failure after");
				assertWithFile("not reached without soft assertions");
				softly.assertAll();
			}).hasMessageContainingAll(
				"lorem ipsum"
			);
		}
	}

}
