package de.cronn.assertions.validationfile.replacements;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public abstract class AbstractJsonReplacer<T extends AbstractJsonReplacer<?>> {

	protected static final String COLON_WITH_WHITESPACES_GROUP = "(\\s?:\\s?)";
	protected String key;

	abstract T getThis();

	abstract ValidationNormalizer build();

	public T withKey(String key) {
		this.key = key;
		return getThis();
	}

}
