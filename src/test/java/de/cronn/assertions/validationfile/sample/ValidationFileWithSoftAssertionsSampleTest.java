package de.cronn.assertions.validationfile.sample;

import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import de.cronn.assertions.validationfile.junit5.JUnit5ValidationFileAssertions;

@Disabled("Enable to demonstrate how soft assertions work")
@ExtendWith(SoftAssertionsExtension.class)
class ValidationFileWithSoftAssertionsSampleTest implements JUnit5ValidationFileAssertions {

	@InjectSoftAssertions
	private SoftAssertions softly;

	@Override
	public FailedAssertionHandler failedAssertionHandler() {
		return callable -> softly.check(callable::call);
	}

	@Test
	void testSoftAssertions() throws Exception {
		assertWithFileWithSuffix("actual1", "file1");
		assertWithFileWithSuffix("actual2", "file2");
	}

}
