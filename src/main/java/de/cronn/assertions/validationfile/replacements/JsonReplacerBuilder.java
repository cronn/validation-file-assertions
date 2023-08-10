package de.cronn.assertions.validationfile.replacements;

import java.util.Objects;
import java.util.regex.Pattern;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public class JsonReplacerBuilder extends AbstractJsonReplacer<JsonReplacerBuilder> {

	private String valueToReplace =
			"(null|true|false|" + // booleans
			"-?\\d+(\\.\\d+)?|" + // numbers
			"(\".+?(?<!\\\\)\"))"; // strings; don't stop matching if it's escaped with \

	private String replacementValue = "[masked]";

	@Override
	public ValidationNormalizer build() {
		Objects.requireNonNull(key);
		Objects.requireNonNull(replacementValue);

		String keyWithQuotationMarks = "\"" + key + "\"";
		Pattern pattern = Pattern.compile(keyWithQuotationMarks + COLON_WITH_WHITESPACES_GROUP + valueToReplace);
		String replacement = keyWithQuotationMarks + "$1" + "\"" + replacementValue + "\"";

		return new Replacer(pattern, replacement);
	}

	/**
	 * @deprecated use withStringValue instead
	 */
	@Deprecated()
	public JsonReplacerBuilder withValue(String value) {
		this.valueToReplace = "\"" + value + "\"";
		return this;
	}

	public JsonReplacerBuilder withStringValue(String value) {
		this.valueToReplace = "\"" + value + "\"";
		return this;
	}

	public JsonReplacerBuilder withRawValue(String value) {
		this.valueToReplace = value;
		return this;
	}

	public JsonReplacerBuilder withReplacement(String replacement) {
		this.replacementValue = replacement;
		return this;
	}

	@Override
	protected JsonReplacerBuilder getThis() {
		return this;
	}
}
