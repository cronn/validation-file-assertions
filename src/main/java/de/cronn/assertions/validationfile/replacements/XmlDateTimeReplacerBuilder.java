package de.cronn.assertions.validationfile.replacements;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public class XmlDateTimeReplacerBuilder extends AbstractXmlReplacerBuilder<XmlDateTimeReplacerBuilder> {
	private DateTimeFormatter destinationFormat;
	private DateTimeFormatter sourceFormat;
	private String dateTimePatternGroup = "(?<DateTime>.+?)";

	public XmlDateTimeReplacerBuilder withDestinationFormat(DateTimeFormatter destinationFormat) {
		this.destinationFormat = destinationFormat;
		return this;
	}

	public XmlDateTimeReplacerBuilder withSourceFormat(DateTimeFormatter sourceFormat) {
		this.sourceFormat = sourceFormat;
		return this;
	}

	private XmlDateTimeReplacerBuilder withDateTimePatternGroup(String dateTimePatternGroup) {
		this.dateTimePatternGroup = dateTimePatternGroup;
		return this;
	}

	@Override
	protected ValidationNormalizer build(String startTag, String endTag) {
		Objects.requireNonNull(sourceFormat);
		Objects.requireNonNull(destinationFormat);

		Pattern pattern = Pattern.compile(startTag + dateTimePatternGroup + endTag);

		return new DateTimeReplacer(pattern, sourceFormat, destinationFormat);
	}

	@Override
	protected XmlDateTimeReplacerBuilder getThis() {
		return this;
	}

	public XmlDateTimeReplacerBuilder withContent(String dateToMatch) {
		return withDateTimePatternGroup("(?<DateTime>" + Pattern.quote(dateToMatch) + ")");
	}
}
