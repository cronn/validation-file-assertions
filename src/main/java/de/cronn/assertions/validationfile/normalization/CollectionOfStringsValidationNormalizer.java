package de.cronn.assertions.validationfile.normalization;

import java.util.Collection;

public class CollectionOfStringsValidationNormalizer implements ValidationNormalizer {

	private final Collection<String> inputStrings;
	private final String outputString;

	public CollectionOfStringsValidationNormalizer(Collection<String> inputStrings, String outputString) {
		this.inputStrings = inputStrings;
		this.outputString = outputString;
	}

	@Override
	public String normalize(String source) {
		String output = source;
		for (String inputString : inputStrings) {
			output = output.replaceAll(inputString, outputString);
		}
		return output;
	}
}
