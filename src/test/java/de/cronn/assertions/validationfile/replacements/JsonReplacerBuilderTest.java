package de.cronn.assertions.validationfile.replacements;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

class JsonReplacerBuilderTest {

	@Test
	void testBuildReplacerForGivenKeyReplacingOnlyGivenValue() throws Exception {
		ValidationNormalizer normalizer = new JsonReplacerBuilder().withKey("key").withStringValue("abcd").build();

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


	private static Stream<Arguments> testBuildReplacerForDifferentValues() {
		return Stream.of(
			Arguments.of(
				"{\"key\": 312.321, \"key\": 123456789, \"key\": -10.4, \"key\": -4}",
				"{\"key\": \"[masked]\", \"key\": \"[masked]\", \"key\": \"[masked]\", \"key\": \"[masked]\"}"
			),
			Arguments.of(
				"{\"key\": \"Lorem, ipsum\", \"author\": \"Human\"}",
				"{\"key\": \"[masked]\", \"author\": \"Human\"}"
			),
			Arguments.of(
				"{\"key\": \"Lorem, ipsum. {not object} [not array] More text \\\"Quote\\\" single \\\" \", \"author\": \"Human\"}",
				"{\"key\": \"[masked]\", \"author\": \"Human\"}"
			),
			Arguments.of(
				"{\"string\": \"Lorem, ipsum\", \"key\": \"Human\"}",
				"{\"string\": \"Lorem, ipsum\", \"key\": \"[masked]\"}"
			),
			Arguments.of(
				"{\"string\": \"Lorem, ipsum\", \"key\": 1000}",
				"{\"string\": \"Lorem, ipsum\", \"key\": \"[masked]\"}"
			),
			Arguments.of(
				"\"key\": \"null\"",
				"\"key\": \"[masked]\""),
			Arguments.of(
				"\"key\": true, \"key\": false",
				"\"key\": \"[masked]\", \"key\": \"[masked]\""
			)
		);
	}

	@ParameterizedTest
	@MethodSource
	void testBuildReplacerForDifferentValues(String toNormalize, String expected) {
		ValidationNormalizer normalizer = new JsonReplacerBuilder().withKey("key").build();
		assertThat(normalizer.normalize(toNormalize)).isEqualTo(expected);
	}
}
