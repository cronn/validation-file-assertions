package de.cronn.assertions.validationfile.normalization;

public final class StringNormalizer {

	private StringNormalizer() {
	}

	public static String normalizeLineEndings(CharSequence string) {
		return normalizeLineEndingsAndTrim(string) + "\n";
	}

	public static String normalizeLineEndingsAndTrim(CharSequence string) {
		return string.toString().replace("\r\n", "\n").trim();
	}

}
