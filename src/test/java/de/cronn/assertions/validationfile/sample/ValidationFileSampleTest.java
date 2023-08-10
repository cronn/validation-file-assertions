package de.cronn.assertions.validationfile.sample;

import static java.time.format.DateTimeFormatter.*;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

import de.cronn.assertions.validationfile.FileExtensions;
import de.cronn.assertions.validationfile.junit5.JUnit5ValidationFileAssertions;
import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.assertions.validationfile.replacements.DateTimeReplacer;
import de.cronn.assertions.validationfile.replacements.Replacer;

class ValidationFileSampleTest implements JUnit5ValidationFileAssertions {

	@Test
	void simpleComparison() throws Exception {
		String actualString = "this will be content of /data/test/validation/ValidationFileSample_simpleComparison.txt";
		assertWithFile(actualString);
	}

	@Test
	void differentFileSuffixes() throws Exception {
		assertWithFileWithSuffix("this will be content of ValidationFileSample_differentFileSuffixes_example1.txt", "example1");
		assertWithFileWithSuffix("this will be content of ValidationFileSample_differentFileSuffixes_example2.txt", "example2");
	}

	@Test
	void differentFileFormats() throws Exception {
		SampleStructure sampleStructure = SampleStructure.filledWithConstantValues();
		assertWithJsonFile(DummySerializer.toJsonString(sampleStructure));
		assertWithXmlFile(DummySerializer.toXmlString(sampleStructure));
		assertWithFile(DummySerializer.toCsvString(sampleStructure), FileExtensions.CSV);
		assertWithFile(sampleStructure.toString(), () -> "str.txt");
	}

	@Test
	void normalization_json() throws Exception {
		SampleStructure sampleStructure = SampleStructure.filledWithRandomValues();

		assertWithJsonFile(DummySerializer.toJsonString(sampleStructure), ValidationNormalizer.combine(
			Replacer.forJson().withKey("MessageId").build(),
			Replacer.forJson().withKey("TransactionId").withRawValue("\\d+").withReplacement("[transaction-id]").build(),
			Replacer.forJsonDateTime().withKey("Timestamp").withSourceFormat(ISO_DATE_TIME).withDestinationFormat(normalizedIsoLocalDate()).build()
		));
	}

	@Test
	void normalization_xml() throws Exception {
		SampleStructure sampleStructure = SampleStructure.filledWithRandomValues();

		assertWithXmlFile(DummySerializer.toXmlString(sampleStructure), ValidationNormalizer.combine(
			Replacer.forXml().withElementName("MessageId").build(),
			Replacer.forXml().withElementName("TransactionId").withContent("\\d+").withReplacement("[transaction-id]").build(),
			Replacer.forXmlDateTime().withElementName("Timestamp").withSourceFormat(ISO_DATE_TIME).withDestinationFormat(normalizedIsoLocalDate()).build()
		));
	}

	@Test
	void normalization_custom() throws Exception {
		SampleStructure sampleStructure = SampleStructure.filledWithRandomValues();

		assertWithFile(sampleStructure.toString(), ValidationNormalizer.combine(
			new Replacer("(messageId)='[\\w-]{36}'", "$1=[masked]"),
			new Replacer("(transactionId)=\\d+", "$1=[transaction-id]"),
			new DateTimeReplacer(Pattern.compile("timestamp=(?<DateTime>[\\d-]+T[\\d:]+\\.\\d+)"), ISO_DATE_TIME, normalizedIsoLocalDate())
		));
	}

	private DateTimeFormatter normalizedIsoLocalDate() {
		return new DateTimeFormatterBuilder()
			.append(ISO_LOCAL_DATE).appendLiteral("T").appendLiteral("[masked]").toFormatter();
	}

}
