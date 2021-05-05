package de.cronn.assertions.validationfile.normalization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class IdNormalizer implements ValidationNormalizer {

	private static final String BACKSLASH = "\\\\";

	/**
	 * non-empty, backslash-escaped (like JSON)
	 */
	public static final String ESCAPED_STRING = "(?:[^" + BACKSLASH + "]|" + BACKSLASH + ".)+?";

	private final boolean doIdMapping;
	private final Collection<Pattern> patterns;
	private final Map<String, String> idMap = new HashMap<>();
	private final IdProvider idProvider;
	private final String idPrefix;

	public IdNormalizer(String... regexp) {
		this(new IncrementingIdProvider(), "", true, regexp);
	}

	public IdNormalizer(IdProvider idProvider, String idPrefix, String... regularExpressions) {
		this(idProvider, idPrefix, true, regularExpressions);
	}

	public IdNormalizer(IdProvider idProvider, String idPrefix, boolean doIdMapping, String... regularExpressions) {
		this.doIdMapping = doIdMapping;
		this.idProvider = idProvider;
		this.idPrefix = idPrefix;
		this.patterns = new ArrayList<>();
		for (String regularExpression : regularExpressions) {
			Pattern pattern = Pattern.compile(regularExpression);
			this.patterns.add(pattern);
		}
	}

	@Override
	public String normalize(String input) {
		String result = input;
		for (Pattern pattern : patterns) {
			result = normalizeOnePattern(result, pattern);
		}
		return result;
	}

	private String normalizeOnePattern(String input, Pattern pattern) {
		Matcher m = pattern.matcher(input);
		StringBuffer result = new StringBuffer();
		while (m.find()) {
			String id = m.group(1);
			String newIdAsString = lookupNewIdAsString(id);
			String stringBeforeGroup = input.substring(m.start(), m.start(1));
			String stringAfterGroup = input.substring(m.end(1), m.end());
			String replacement = stringBeforeGroup + idPrefix + newIdAsString + stringAfterGroup;
			String escapedReplacement = replacement.replace("\\", "\\\\");
			escapedReplacement = escapedReplacement.replace("$", "\\$");
			m.appendReplacement(result, escapedReplacement);
		}
		m.appendTail(result);

		return result.toString();
	}

	private String lookupNewIdAsString(String originalId) {
		String newIdAsString = null;
		if (doIdMapping) {
			newIdAsString = idMap.get(originalId);
		}
		if (newIdAsString == null) {
			long newId = idProvider.next();
			newIdAsString = String.valueOf(newId);
			if (doIdMapping) {
				idMap.put(originalId, newIdAsString);
			}
		}
		return newIdAsString;
	}

}
