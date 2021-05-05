package de.cronn.assertions.validationfile.replacements;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;

public class DateTimeReplacer implements ValidationNormalizer {
	private static final String DATE_TIME_GROUP_NAME = "DateTime";

	private Matcher matcher;
	private DateTimeFormatter sourceFormat;
	private DateTimeFormatter destinationFormat;

	public DateTimeReplacer(Pattern pattern, DateTimeFormatter sourceFormat, DateTimeFormatter destinationFormat) {
		this.matcher = pattern.matcher("");
		this.sourceFormat = sourceFormat;
		this.destinationFormat = destinationFormat;
	}

	@Override
	public String normalize(String textToNormalize) {
		StringBuilder normalizedResultBuilder = new StringBuilder();

		matcher.reset(textToNormalize);
		int endOfLastMatchIndex = 0;

		boolean matchFound = false;

		while (matcher.find()) {
			matchFound = true;

			int startOfCurrentMatchIndex = matcher.start();
			int endOfCurrentMatchIndex = matcher.end();

			appendFromSourceToResult(textToNormalize, normalizedResultBuilder, endOfLastMatchIndex, startOfCurrentMatchIndex);
			String match = textToNormalize.substring(startOfCurrentMatchIndex, endOfCurrentMatchIndex);

			try {
				String possiblyDateTime = matchDateTime(textToNormalize);
				String formattedDateTime = matchDateTimeAndConvertFromSourceToDestinationFormat(possiblyDateTime);
				normalizedResultBuilder.append(match.replace(possiblyDateTime, formattedDateTime));
			} catch (DateTimeParseException e) {
				normalizedResultBuilder.append(match);
			}

			endOfLastMatchIndex = endOfCurrentMatchIndex;
		}

		if (!matchFound) {
			return textToNormalize;
		}

		appendFromSourceToResult(textToNormalize, normalizedResultBuilder,
			endOfLastMatchIndex, textToNormalize.length());

		return normalizedResultBuilder.toString();
	}

	private String matchDateTimeAndConvertFromSourceToDestinationFormat(String possiblyDateTime) {
		TemporalAccessor parsedDateTime = sourceFormat.parse(possiblyDateTime);
		return destinationFormat.format(parsedDateTime);
	}

	private String matchDateTime(String text) {
		return text.substring(matcher.start(DATE_TIME_GROUP_NAME), matcher.end(DATE_TIME_GROUP_NAME));
	}

	private void appendFromSourceToResult(String source, StringBuilder resultBuilder, int fromIndex, int toIndex) {
		resultBuilder.append(source, fromIndex, toIndex);
	}

	@Override
	public String toString() {
		return "DateTimeReplacer for pattern " + matcher.pattern().toString() + ".";
	}

}
