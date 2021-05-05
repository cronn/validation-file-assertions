package de.cronn.assertions.validationfile.replacements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public class Replacer implements ValidationNormalizer {
	private final Matcher matcher;
	private final String replacement;

	public Replacer(String content, String replacement) {
		this(Pattern.compile(content), replacement);
	}

	public Replacer(Pattern pattern, String replacement) {
		this.matcher = pattern.matcher("");
		this.replacement = replacement;
	}

	@Override
	public String normalize(String source) {
		return matcher.reset(source).replaceAll(replacement);
	}

	public static XmlReplacerBuilder forXml() {
		return new XmlReplacerBuilder();
	}

	public static XmlDateTimeReplacerBuilder forXmlDateTime() {
		return new XmlDateTimeReplacerBuilder();
	}

	public static JsonReplacerBuilder forJson() {
		return new JsonReplacerBuilder();
	}

	public static JsonDateTimeReplacerBuilder forJsonDateTime() {
		return new JsonDateTimeReplacerBuilder();
	}

	@Override
	public String toString() {
		return "Replacer for pattern " + matcher.pattern().toString() + " with replacement " + replacement + ".";
	}
}
