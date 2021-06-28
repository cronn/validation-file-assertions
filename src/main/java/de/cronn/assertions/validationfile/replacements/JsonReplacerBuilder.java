package de.cronn.assertions.validationfile.replacements;

import java.util.Objects;
import java.util.regex.Pattern;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public class JsonReplacerBuilder extends AbstractJsonReplacer<JsonReplacerBuilder> {

	private String valueToReplace = ".+?";
	private String replacementValue = "[masked]";

	@Override
	public ValidationNormalizer build() {
		Objects.requireNonNull(key);
		Objects.requireNonNull(replacementValue);

		String keyWithQuotationMarks = "\"" + key + "\"";
		Pattern pattern = Pattern.compile(keyWithQuotationMarks + COLON_WITH_WHITESPACES_GROUP + "\"" + valueToReplace + "\"");
		String replacement = keyWithQuotationMarks + "$1" + "\"" + replacementValue + "\"";

		return new Replacer(pattern, replacement);
	}

	public JsonReplacerBuilder withValue(String value) {
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
