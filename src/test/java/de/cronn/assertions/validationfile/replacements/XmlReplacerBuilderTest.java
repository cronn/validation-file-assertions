package de.cronn.assertions.validationfile.replacements;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

class XmlReplacerBuilderTest {

	@Test
	void testWithContent() throws Exception {
		XmlReplacerBuilder xmlReplacerBuilder = new XmlReplacerBuilder();
		ValidationNormalizer normalizer = xmlReplacerBuilder.withElementName("elem").withContent("abcd").build();

		String actual = normalizer.normalize("<elem>abcd</elem>");
		String expected = "<elem>[masked]</elem>";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testDefaultContentMatchesAnything() throws Exception {
		XmlReplacerBuilder xmlReplacerBuilder = new XmlReplacerBuilder();
		ValidationNormalizer normalizer = xmlReplacerBuilder.withElementName("elem").build();

		String actual = normalizer.normalize("<elem>123 \\ sdf abcd</elem>");
		String expected = "<elem>[masked]</elem>";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testDefaultContentMatchesAnythingButLineBreaks() throws Exception {
		XmlReplacerBuilder xmlReplacerBuilder = new XmlReplacerBuilder();
		ValidationNormalizer normalizer = xmlReplacerBuilder.withElementName("elem").build();

		String source = "<elem>123 \\ sdf \n abcd</elem>";
		String actual = normalizer.normalize(source);

		assertThat(actual).isEqualTo(source);
	}

	@Test
	void testWithReplacement() throws Exception {
		XmlReplacerBuilder xmlReplacerBuilder = new XmlReplacerBuilder();
		ValidationNormalizer normalizer = xmlReplacerBuilder.withElementName("elem").withReplacement("[replaced]").build();

		String actual = normalizer.normalize("<elem>abcd</elem>");
		String expected = "<elem>[replaced]</elem>";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testWithSomeContent() throws Exception {
		XmlReplacerBuilder xmlReplacerBuilder = new XmlReplacerBuilder();
		ValidationNormalizer normalizer = xmlReplacerBuilder.withElementName("elem").withSomeContent().build();

		String actual = normalizer.normalize("<elem>abcd</elem>");
		String expected = "<elem>[masked]</elem>";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testWithSomeDigitsContent() throws Exception {
		XmlReplacerBuilder xmlReplacerBuilder = new XmlReplacerBuilder();
		ValidationNormalizer normalizer = xmlReplacerBuilder.withElementName("elem").withSomeDigitsContent().build();

		String actual = normalizer.normalize("<elem>12345</elem>");
		String expected = "<elem>[masked]</elem>";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testWithSomeDigitsContentDoesNotMatchNonDigitContent() throws Exception {
		XmlReplacerBuilder xmlReplacerBuilder = new XmlReplacerBuilder();
		ValidationNormalizer normalizer = xmlReplacerBuilder.withElementName("elem").withSomeDigitsContent().build();

		String actual = normalizer.normalize("<elem>abcd</elem>");
		String expected = "<elem>abcd</elem>";

		assertThat(actual).isEqualTo(expected);
	}

	@Test
	void testGetThis() throws Exception {
		XmlReplacerBuilder xmlReplacerBuilder = new XmlReplacerBuilder();
		assertThat(xmlReplacerBuilder).isEqualTo(xmlReplacerBuilder.getThis());
	}

}
