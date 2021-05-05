package de.cronn.assertions.validationfile;

public enum FileExtensions implements FileExtension {
	TXT("txt"),
	XML("xml"),
	JSON("json"),
	DIFF("diff"),
	CSV("csv"),
	MD("md"),
	;

	private final String value;

	FileExtensions(String value) {
		this.value = value;
	}

	@Override
	public String asString() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}
}
