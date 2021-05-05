package de.cronn.assertions.validationfile.replacements;

import static java.time.format.DateTimeFormatter.*;
import static org.assertj.core.api.Assertions.*;

import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

class XmlDateTimeReplacerBuilderTest {

	@Test
	void testBuildXMLDateTimeReplacer() {
		XmlDateTimeReplacerBuilder xmlDateTimeReplacerBuilder = new XmlDateTimeReplacerBuilder();
		ValidationNormalizer normalizer = xmlDateTimeReplacerBuilder.withElementName("elem").withSourceFormat(ISO_OFFSET_DATE_TIME)
			.withDestinationFormat(DateTimeFormatter.ofPattern("YYYY")).build();

		String actual = normalizer.normalize("<elem>2011-12-03T10:15:30+01:00</elem>");
		String expected = "<elem>2011</elem>";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testBuildXMLDateTimeReplacerWithExactDateToMatch() {
		String dateToMatch = "2011-12-03T10:15:30+01:00";
		XmlDateTimeReplacerBuilder xmlDateTimeReplacerBuilder = new XmlDateTimeReplacerBuilder();
		ValidationNormalizer normalizer = xmlDateTimeReplacerBuilder
			.withElementName("elem")
			.withContent(dateToMatch)
			.withSourceFormat(ISO_OFFSET_DATE_TIME)
			.withDestinationFormat(DateTimeFormatter.ofPattern("YYYY")).build();

		String actual = normalizer.normalize("<elem>2011-12-03T10:15:30+01:00</elem>");
		String expected = "<elem>2011</elem>";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testBuildXMLDateTimeReplacerWithExactDateToMatch_noMatch() {
		String dateToMatch = "2017-12-03T10:15:30+01:00";
		XmlDateTimeReplacerBuilder xmlDateTimeReplacerBuilder = new XmlDateTimeReplacerBuilder();
		ValidationNormalizer normalizer = xmlDateTimeReplacerBuilder
			.withElementName("elem")
			.withContent(dateToMatch)
			.withSourceFormat(ISO_OFFSET_DATE_TIME)
			.withDestinationFormat(DateTimeFormatter.ofPattern("YYYY")).build();

		String source = "<elem>2011-12-03T10:15:30+01:00</elem>";
		String actual = normalizer.normalize(source);

		assertThat(actual).isEqualTo(source);
	}

	@Test
	void testGetThis() throws Exception {
		XmlDateTimeReplacerBuilder xmlDateTimeReplacerBuilder = new XmlDateTimeReplacerBuilder();
		assertThat(xmlDateTimeReplacerBuilder.getThis()).isEqualTo(xmlDateTimeReplacerBuilder);
	}

}
