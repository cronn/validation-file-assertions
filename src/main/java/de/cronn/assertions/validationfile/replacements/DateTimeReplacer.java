package de.cronn.assertions.validationfile.replacements;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTimeReplacer implements ValidationNormalizer {
  private static final String DATE_TIME_GROUP_NAME = "DateTime";

  private final Pattern pattern;
  private final DateTimeFormatter sourceFormat;
  private final DateTimeFormatter destinationFormat;

  public DateTimeReplacer(
      Pattern pattern, DateTimeFormatter sourceFormat, DateTimeFormatter destinationFormat) {
    this.pattern = pattern;
    this.sourceFormat = sourceFormat;
    this.destinationFormat = destinationFormat;
  }

  @Override
  public String normalize(String textToNormalize) {
    StringBuilder normalizedResultBuilder = new StringBuilder();

    Matcher matcher = pattern.matcher(textToNormalize);
    int endOfLastMatchIndex = 0;

    boolean matchFound = false;

    while (matcher.find()) {
      matchFound = true;

      int startOfCurrentMatchIndex = matcher.start();
      int endOfCurrentMatchIndex = matcher.end();

      appendFromSourceToResult(
          textToNormalize, normalizedResultBuilder, endOfLastMatchIndex, startOfCurrentMatchIndex);
      String match = textToNormalize.substring(startOfCurrentMatchIndex, endOfCurrentMatchIndex);

      try {
        String possiblyDateTime = matchDateTime(textToNormalize, matcher);
        String formattedDateTime =
            matchDateTimeAndConvertFromSourceToDestinationFormat(possiblyDateTime);
        normalizedResultBuilder.append(match.replace(possiblyDateTime, formattedDateTime));
      } catch (DateTimeParseException e) {
        normalizedResultBuilder.append(match);
      }

      endOfLastMatchIndex = endOfCurrentMatchIndex;
    }

    if (!matchFound) {
      return textToNormalize;
    }

    appendFromSourceToResult(
        textToNormalize, normalizedResultBuilder, endOfLastMatchIndex, textToNormalize.length());

    return normalizedResultBuilder.toString();
  }

  private String matchDateTimeAndConvertFromSourceToDestinationFormat(String possiblyDateTime) {
    TemporalAccessor parsedDateTime = sourceFormat.parse(possiblyDateTime);
    return destinationFormat.format(parsedDateTime);
  }

  private String matchDateTime(String text, Matcher matcher) {
    return text.substring(matcher.start(DATE_TIME_GROUP_NAME), matcher.end(DATE_TIME_GROUP_NAME));
  }

  private void appendFromSourceToResult(
      String source, StringBuilder resultBuilder, int fromIndex, int toIndex) {
    resultBuilder.append(source, fromIndex, toIndex);
  }

  @Override
  public String toString() {
    return "DateTimeReplacer for pattern " + pattern.toString() + ".";
  }
}
