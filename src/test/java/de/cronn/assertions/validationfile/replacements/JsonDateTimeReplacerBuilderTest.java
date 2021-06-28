package de.cronn.assertions.validationfile.replacements;

import static java.time.format.DateTimeFormatter.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

class JsonDateTimeReplacerBuilderTest {

	@Test
	void testReplaceDateTimeInJson() throws Exception {
		String json = "\"someThing\": \"foo\", \"dateTime1\": \"2017-09-04T15:39:31.31+02:00\", " +
			"\"dateTime2\": \"2017-09-04T15:39:31.31+02:00\", \"someThing\": \"bar\"";

		ValidationNormalizer normalizer = new JsonDateTimeReplacerBuilder().withKey("dateTime1")
			.withSourceFormat(ISO_OFFSET_DATE_TIME).withDestinationFormat(ISO_LOCAL_DATE).build();

		String actual = normalizer.normalize(json);
		String expected = "\"someThing\": \"foo\", \"dateTime1\": \"2017-09-04\", " +
			"\"dateTime2\": \"2017-09-04T15:39:31.31+02:00\", \"someThing\": \"bar\"";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testReplaceDateTimeInJsonWithCustomColonGroupRegex_match() {
		String json = "\"dateTime\": \"2017-09-04T15:39:31.31+02:00\"";
		String colonGroupRegex = "(:\\s)";

		ValidationNormalizer normalizer = new JsonDateTimeReplacerBuilder()
			.withKey("dateTime").withColonGroupRegex(colonGroupRegex)
			.withSourceFormat(ISO_OFFSET_DATE_TIME).withDestinationFormat(ISO_LOCAL_DATE).build();

		String expected = "\"dateTime\": \"2017-09-04\"";
		String actual = normalizer.normalize(json);

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testReplaceDateTimeInJsonWithCustomColonGroupRegex_noMatch() {
		String json = "\"dateTime\": \"2017-09-04T15:39:31.31+02:00\"";
		String colonGroupRegex = "(\\s:\\s)";

		ValidationNormalizer normalizer = new JsonDateTimeReplacerBuilder()
			.withKey("dateTime").withColonGroupRegex(colonGroupRegex)
			.withSourceFormat(ISO_OFFSET_DATE_TIME).withDestinationFormat(ISO_LOCAL_DATE).build();

		String actual = normalizer.normalize(json);

		assertThat(actual).isEqualTo(json);
	}
	@Test
	void testGetThis() throws Exception {
		JsonDateTimeReplacerBuilder jsonDateTimeReplacerBuilder = new JsonDateTimeReplacerBuilder();
		assertThat(jsonDateTimeReplacerBuilder.getThis()).isEqualTo(jsonDateTimeReplacerBuilder);
	}

}
