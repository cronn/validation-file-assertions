package de.cronn.assertions.validationfile.normalization;

public class IncrementingIdProvider implements IdProvider {

	private long current;

	public IncrementingIdProvider() {
		reset();
	}

	public void reset() {
		current = 0;
	}

	@Override
	public long next() {
		return ++current;
	}

}
