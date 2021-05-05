package de.cronn.assertions.validationfile.normalization;

import java.util.Objects;

public class SimpleRegexReplacement implements ValidationNormalizer {

	private String regex;

	private String replacement;

	public SimpleRegexReplacement(String regex, String replacement) {
		Objects.requireNonNull(regex);
		this.regex = regex;
		this.replacement = replacement;
	}

	public SimpleRegexReplacement(String regex) {
		this(regex, "[masked]");
	}

	public String apply(String source) {
		return source.replaceAll(regex, replacement);
	}

	public String getRegex() {
		return regex;
	}

	public String getReplacement() {
		return replacement;
	}

	@Override
	public String normalize(String source) {
		return apply(source);
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public void setReplacement(String replacement) {
		this.replacement = replacement;
	}

}
