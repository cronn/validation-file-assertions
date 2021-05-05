package de.cronn.assertions.validationfile.replacements;

import static java.time.format.DateTimeFormatter.*;
import static org.assertj.core.api.Assertions.*;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

class DateTimeReplacerTest {
	@Test
	void testReturnsInputObjectIfNoMatchIsFound() throws Exception {
		String nonMatchingString = "doesNotMatch";

		DateTimeReplacer dateTimeReplacer = new DateTimeReplacer(Pattern.compile("foo (?<DateTime>.+?) bar"),
			ISO_OFFSET_DATE_TIME, ISO_LOCAL_DATE);

		String normalized = dateTimeReplacer.normalize(nonMatchingString);
		assertThat(normalized).isSameAs(nonMatchingString);
	}

	@Test
	void testReplaceDateTimeStandalone() throws Exception {
		String dateTime = "2017-09-04T15:39:31.31+02:00";
		DateTimeReplacer dateTimeReplacer = new DateTimeReplacer(Pattern.compile("(?<DateTime>.+)"),
			ISO_OFFSET_DATE_TIME, ISO_LOCAL_DATE);

		String actual = dateTimeReplacer.normalize(dateTime);
		String expected = "2017-09-04";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testReplaceDateTimeEncapsulated() throws Exception {
		String dateTime = "foo 2017-09-04T15:39:31.31+02:00 bar";
		DateTimeReplacer dateTimeReplacer = new DateTimeReplacer(Pattern.compile("foo (?<DateTime>.+?) bar"),
			ISO_OFFSET_DATE_TIME, ISO_LOCAL_DATE);

		String actual = dateTimeReplacer.normalize(dateTime);
		String expected = "foo 2017-09-04 bar";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testReplaceDateTimeEncapsulatedNoMatch() throws Exception {
		String dateTime = "foo 2017-09-04T15:39:31.31+02:00 bar";
		DateTimeReplacer dateTimeReplacer = new DateTimeReplacer(Pattern.compile("foo (?<DateTime>.+?) bar"),
			DateTimeFormatter.ofPattern("YYYY"), ISO_LOCAL_DATE);

		String actual = dateTimeReplacer.normalize(dateTime);
		String expected = "foo 2017-09-04T15:39:31.31+02:00 bar";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testReplaceMultipleDateTime() throws Exception {
		String dateTime = "x 2017-09-04T15:39:31.31+02:00 x " +
			"x 2016-09-04T15:39:31.31+02:00 x " +
			"x 2015-09-04T15:39:31.31+02:00 x";
		DateTimeReplacer dateTimeReplacer = new DateTimeReplacer(Pattern.compile("x (?<DateTime>.+?) x"),
			ISO_OFFSET_DATE_TIME, ISO_LOCAL_DATE);

		String actual = dateTimeReplacer.normalize(dateTime);
		String expected = "x 2017-09-04 x x 2016-09-04 x x 2015-09-04 x";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testReplaceDateTimeInXml() {
		String xml = "<someThing>foo</someThing> <dateTime1>2017-09-04T15:39:31.31+02:00</dateTime1> " +
			"<dateTime2>2017-09-04T15:39:31.31+02:00</dateTime2> <someThing>bar</someThing>";

		ValidationNormalizer normalizer = new XmlDateTimeReplacerBuilder().withElementName("dateTime1")
			.withSourceFormat(ISO_OFFSET_DATE_TIME).withDestinationFormat(ISO_LOCAL_DATE).build();

		String actual = normalizer.normalize(xml);
		String expected = "<someThing>foo</someThing> <dateTime1>2017-09-04</dateTime1> " +
			"<dateTime2>2017-09-04T15:39:31.31+02:00</dateTime2> <someThing>bar</someThing>";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testToString() throws Exception {
		ValidationNormalizer normalizer = new XmlDateTimeReplacerBuilder().withElementName("date")
			.withSourceFormat(ISO_OFFSET_DATE_TIME).withDestinationFormat(ISO_LOCAL_DATE).build();

		String expected = "DateTimeReplacer for pattern <date>(?<DateTime>.+?)</date>.";
		String actual = normalizer.toString();

		assertThat(actual).isEqualTo(expected);
	}

}
