package de.cronn.assertions.validationfile.replacements;

import static org.assertj.core.api.Assertions.*;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

class ReplacerTest {

	@Test
	void testNormalizeAlreadyCompiledPattern() throws Exception {
		Replacer replacer = new Replacer(Pattern.compile("a[b]*?c"), "replacement");

		String expected = "replacement, replacement, ab, a, c, bc";
		String actual = replacer.normalize("abc, ac, ab, a, c, bc");

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testNormalize() throws Exception {
		Replacer replacer = new Replacer("a[b]*?c", "replacement");

		String expected = "replacement, replacement, ab, a, c, bc";
		String actual = replacer.normalize("abc, ac, ab, a, c, bc");

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testForXml() throws Exception {
		XmlReplacerBuilder xmlReplacerBuilder = Replacer.forXml();
		assertThat(xmlReplacerBuilder).isNotNull();
	}

	@Test
	void testForXmlDateTime() throws Exception {
		XmlDateTimeReplacerBuilder xmlDateTimeReplacerBuilder = Replacer.forXmlDateTime();
		assertThat(xmlDateTimeReplacerBuilder).isNotNull();
	}

	@Test
	void testForJson() throws Exception {
		JsonReplacerBuilder jsonReplacerBuilder = Replacer.forJson();
		assertThat(jsonReplacerBuilder).isNotNull();
	}

	@Test
	void testForJsonDateTime() throws Exception {
		JsonDateTimeReplacerBuilder jsonDateTimeReplacerBuilder = Replacer.forJsonDateTime();
		assertThat(jsonDateTimeReplacerBuilder).isNotNull();
	}

	@Test
	void testToString() throws Exception {
		Replacer replacer = new Replacer(Pattern.compile("a[b]c"), "a");
		String expected = "Replacer for pattern a[b]c with replacement a.";
		String actual = replacer.toString();

		assertThat(actual).isEqualTo(expected);
	}


}
