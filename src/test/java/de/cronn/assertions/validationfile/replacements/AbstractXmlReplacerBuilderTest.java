package de.cronn.assertions.validationfile.replacements;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

class AbstractXmlReplacerBuilderTest {
	static class XmlReplacerBuilderForTest extends AbstractXmlReplacerBuilder<XmlReplacerBuilderForTest> {

		@Override
		protected XmlReplacerBuilderForTest getThis() {
			return this;
		}

		@Override
		protected ValidationNormalizer build(String startTag, String endTag) {
			throw new RuntimeException("not to be used");
		}
	}

	@Test
	void testCreateEndTagWithoutNamespace() throws Exception {
		XmlReplacerBuilderForTest xmlReplacerBuilderForTest = new XmlReplacerBuilderForTest();
		xmlReplacerBuilderForTest.withElementName("elem");

		String endTag = xmlReplacerBuilderForTest.createEndTag();

		assertThat(endTag).isEqualTo("</elem>");
	}

	@Test
	void testCreateEndTagWithNamespace() throws Exception {
		XmlReplacerBuilderForTest xmlReplacerBuilder = new XmlReplacerBuilderForTest()
			.withNamespace("ns")
			.withElementName("elem");

		String actual = xmlReplacerBuilder.createEndTag();

		assertThat(actual).isEqualTo("</ns:elem>");
	}

	@Test
	void testCreateStartTagWithNamespace() throws Exception {
		XmlReplacerBuilderForTest xmlReplacerBuilder = new XmlReplacerBuilderForTest()
			.withNamespace("ns")
			.withElementName("elem");

		String startTag = xmlReplacerBuilder.createStartTag();

		assertThat(startTag).isEqualTo("<ns:elem>");
	}

	@Test
	void testCreateStartTagWithoutNamespace() throws Exception {
		XmlReplacerBuilderForTest xmlReplacerBuilder = new XmlReplacerBuilderForTest()
			.withElementName("elem");

		String startTag = xmlReplacerBuilder.createStartTag();

		assertThat(startTag).isEqualTo("<elem>");
	}

	@Test
	void testCreateStartTagWithAttributeWithoutNamespace() throws Exception {
		XmlReplacerBuilderForTest xmlReplacerBuilder = new XmlReplacerBuilderForTest()
			.withElementName("elem")
			.withAttribute("attr", "val");

		String startTag = xmlReplacerBuilder.createStartTag();

		assertThat(startTag).isEqualTo("<elem attr=\"val\">");
	}

	@Test
	void testCreateStartTagWithAttributeAndNamespace() throws Exception {
		XmlReplacerBuilderForTest xmlReplacerBuilder = new XmlReplacerBuilderForTest()
			.withNamespace("ns")
			.withElementName("elem")
			.withAttribute("attr", "val");

		String startTag = xmlReplacerBuilder.createStartTag();

		assertThat(startTag).isEqualTo("<ns:elem attr=\"val\">");
	}

	@Test
	void testCreateStartTagWithMultipleAttributesAndNamespace() throws Exception {
		XmlReplacerBuilderForTest xmlReplacerBuilder = new XmlReplacerBuilderForTest()
			.withNamespace("ns")
			.withElementName("elem")
			.withAttribute("firstAttr", "firstVal")
			.withAttribute("secondAttr", "secondVal");

		String startTag = xmlReplacerBuilder.createStartTag();

		assertThat(startTag).isEqualTo("<ns:elem firstAttr=\"firstVal\" secondAttr=\"secondVal\">");
	}

}
