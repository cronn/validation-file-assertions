package de.cronn.assertions.validationfile.replacements;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Pattern;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public class JsonDateTimeReplacerBuilder extends AbstractJsonReplacer<JsonDateTimeReplacerBuilder> {
	private DateTimeFormatter sourceFormat;
	private DateTimeFormatter destinationFormat;
	private String colonGroupRegex = COLON_WITH_WHITESPACES_GROUP;

	public JsonDateTimeReplacerBuilder withDestinationFormat(DateTimeFormatter destinationFormat) {
		this.destinationFormat = destinationFormat;
		return this;
	}

	public JsonDateTimeReplacerBuilder withSourceFormat(DateTimeFormatter sourceFormat) {
		this.sourceFormat = sourceFormat;
		return this;
	}

	@Override
	public ValidationNormalizer build() {
		Objects.requireNonNull(key);

		String keyWithQuotationMarks = "\"" + key + "\"";
		Pattern pattern = Pattern.compile(keyWithQuotationMarks + colonGroupRegex + "\"(?<DateTime>.+?)\"");

		return new DateTimeReplacer(pattern, sourceFormat, destinationFormat);
	}

	@Override
	JsonDateTimeReplacerBuilder getThis() {
		return this;
	}


}
