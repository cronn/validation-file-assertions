package de.cronn.assertions.validationfile.replacements;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

class JsonReplacerBuilderTest {

	@Test
	void testBuildReplacerForGivenKeyReplacingAnyValue() throws Exception {
		ValidationNormalizer normalizer = new JsonReplacerBuilder().withKey("key").build();

		String expected = "\"key\": \"[masked]\", \"other\": \"foobar\"";
		String actual = normalizer.normalize("\"key\": \"foobar\", \"other\": \"foobar\"");

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testBuildReplacerForGivenKeyReplacingOnlyGivenValue() throws Exception {
		ValidationNormalizer normalizer = new JsonReplacerBuilder().withKey("key").withValue("abcd").build();

		String expected = "\"key\": \"[masked]\", \"key\": \"foobar\"";
		String actual = normalizer.normalize("\"key\": \"abcd\", \"key\": \"foobar\"");

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testBuildReplacerWithCustomReplacement() throws Exception {
		ValidationNormalizer normalizer = new JsonReplacerBuilder().withKey("key").withReplacement("[replaced]").build();

		String expected = "\"key\": \"[replaced]\", \"other\": \"foobar\"";
		String actual = normalizer.normalize("\"key\": \"foobar\", \"other\": \"foobar\"");

		assertThat(actual).isEqualTo(expected);
	}

}
