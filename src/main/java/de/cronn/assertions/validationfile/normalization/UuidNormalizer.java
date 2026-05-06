package de.cronn.assertions.validationfile.normalization;

public class UuidNormalizer implements ValidationNormalizer {

	private final ValidationNormalizer delegate =
		new IdNormalizer(
			new IncrementingIdProvider(),
			"UUID_",
			"([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12})");

	@Override
	public String normalize(String source) {
		return delegate.normalize(source);
	}

}
