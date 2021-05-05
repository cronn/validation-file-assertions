package de.cronn.assertions.validationfile.replacements;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public abstract class AbstractXmlReplacerBuilder<T extends AbstractXmlReplacerBuilder<?>> {

	private String namespace;
	private String elementName;
	private final Map<String, String> attributes = new LinkedHashMap<>();

	protected abstract T getThis();

	protected abstract ValidationNormalizer build(String startTag, String endTag);

	public T withNamespace(String namespace) {
		this.namespace = namespace;
		return getThis();
	}

	public T withElementName(String attribute) {
		this.elementName = attribute;
		return getThis();
	}

	public T withAttribute(String name, String value) {
		attributes.put(name, value);

		return getThis();
	}

	String createEndTag() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("</");
		if (namespace != null) {
			stringBuilder.append(namespace).append(":");
		}

		stringBuilder.append(elementName);

		stringBuilder.append(">");

		return stringBuilder.toString();
	}

	String createStartTag() {
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<");
		if (namespace != null) {
			stringBuilder.append(namespace).append(":");
		}

		stringBuilder.append(elementName);
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();
			stringBuilder.append(" ");
			stringBuilder.append(attributeName);

			String attributeValue = entry.getValue();
			if (attributeValue != null) {
				stringBuilder.append("=\"");
				stringBuilder.append(attributeValue);
				stringBuilder.append("\"");
			}
		}

		stringBuilder.append(">");

		return stringBuilder.toString();
	}

	public final ValidationNormalizer build() {
		Objects.requireNonNull(elementName);

		String startTag = createStartTag();
		String endTag = createEndTag();

		return build(startTag, endTag);
	}

}
