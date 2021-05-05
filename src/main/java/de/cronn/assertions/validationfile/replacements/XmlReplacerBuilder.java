package de.cronn.assertions.validationfile.replacements;

import java.util.Objects;
import java.util.regex.Pattern;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public class XmlReplacerBuilder extends AbstractXmlReplacerBuilder<XmlReplacerBuilder> {
	public static final String DEFAULT_MASK = "[masked]";

	private String contentToReplace;
	private String replacementContent;

	public XmlReplacerBuilder() {
		withSomeContent().withReplacement(DEFAULT_MASK);
	}

	public XmlReplacerBuilder withContent(String content) {
		this.contentToReplace = content;
		return this;
	}

	public XmlReplacerBuilder withReplacement(String content) {
		this.replacementContent = content;
		return this;
	}

	public XmlReplacerBuilder withSomeContent() {
		contentToReplace = ".+?";
		return this;
	}

	public XmlReplacerBuilder withSomeDigitsContent() {
		contentToReplace = "[0-9]+";
		return this;
	}

	@Override
	protected ValidationNormalizer build(String startTag, String endTag) {
		Objects.requireNonNull(contentToReplace);
		Objects.requireNonNull(replacementContent);

		Pattern pattern = Pattern.compile(startTag + contentToReplace + endTag);
		String replacement = startTag + replacementContent + endTag;

		return new Replacer(pattern, replacement);
	}

	@Override
	protected XmlReplacerBuilder getThis() {
		return this;
	}
}
