package de.cronn.assertions.validationfile.normalization;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

class ListOfStringsValidationNormalizerTest {

	@Test
	void testNormalize() throws Exception {
		// given
		String input = "Test id-1 and id-2 and id-4";
		String expected = "Test [valid-id] and [valid-id] and [valid-id]";

		// when
		String normalized = new CollectionOfStringsValidationNormalizer(
			Arrays.asList("id-1", "id-2", "id-3", "id-4"), "[valid-id]")
			.normalize(input);

		// then
		assertThat(normalized).isEqualTo(expected);
	}
}
