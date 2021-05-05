package de.cronn.assertions.validationfile.normalization;

public class ConstantIdProvider implements IdProvider {

	private long value;

	public ConstantIdProvider(long value) {
		this.value = value;
	}

	@Override
	public long next() {
		return value;
	}

}
