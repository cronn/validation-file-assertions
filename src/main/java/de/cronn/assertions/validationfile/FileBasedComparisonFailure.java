package de.cronn.assertions.validationfile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.opentest4j.AssertionFailedError;

import difflib.Delta;
import difflib.DiffUtils;
import difflib.Patch;

public class FileBasedComparisonFailure extends AssertionFailedError {

	private static final long serialVersionUID = 1L;

	// these fields might look unnecessary, but they are needed to support IntelliJ diff viewer
	// details: https://github.com/JetBrains/intellij-community/commit/b04f577ff6897d011a4dbaa3e381a67d054fb0a2#diff-8ec837b964197c27c51323151bc3eebbR248
	private final String fExpected;

	private final String fActual;

	public FileBasedComparisonFailure(String expected, String actual, String filenameExpected, String filenameActual) {
		super(buildDiff(expected, actual, filenameExpected, filenameActual), expected, actual);
		this.fExpected = expected;
		this.fActual = actual;
	}

	private static String buildDiff(String expected, String actual, String filenameExpected, String filenameActual) {
		List<String> expectedLines = expected == null ? Collections.emptyList() : splitLines(expected);
		List<String> actualLines = actual == null ? Collections.emptyList() : splitLines(actual);

		Patch<String> patch = DiffUtils.diff(expectedLines, actualLines);
		List<Delta<String>> deltas = patch.getDeltas();
		if (deltas.isEmpty()) {
			throw new IllegalArgumentException("expected and actual values are equal");
		}
		return "\n" + String.join(
			"\n",
			DiffUtils.generateUnifiedDiff(
				"expected" + (filenameExpected != null ? "/" + filenameExpected : ""),
				"actual" + (filenameActual != null ? "/" + filenameActual : ""),
				expectedLines,
				patch,
				3
			)
		);
	}

	private static List<String> splitLines(String textToSplit) {
		return Arrays.asList(textToSplit.split("\n"));
	}

}
