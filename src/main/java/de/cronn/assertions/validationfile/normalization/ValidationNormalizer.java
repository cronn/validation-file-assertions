package de.cronn.assertions.validationfile.normalization;

import java.util.Arrays;
import java.util.Collection;

@FunctionalInterface
public interface ValidationNormalizer {

	String normalize(String source);

	default ValidationNormalizer and(ValidationNormalizer otherNormalizer) {
		return ValidationNormalizer.combine(this, otherNormalizer);
	}

	static ValidationNormalizer doNothing() {
		return s -> s;
	}

	static ValidationNormalizer combine(ValidationNormalizer... normalizers) {
		return combine(Arrays.asList(normalizers));
	}

	static ValidationNormalizer combine(Collection<ValidationNormalizer> normalizers) {
		return s -> applyNormalizers(s, normalizers);
	}

	static String applyNormalizers(String input, Collection<ValidationNormalizer> normalizers) {
		String normalized = input;
		for (ValidationNormalizer validationNormalizer : normalizers) {
			normalized = validationNormalizer.normalize(normalized);
		}
		return normalized;
	}

}
