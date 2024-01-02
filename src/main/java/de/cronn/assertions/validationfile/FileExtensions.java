package de.cronn.assertions.validationfile;

public enum FileExtensions implements FileExtension {
	TXT("txt"),
	XML("xml"),
	JSON("json"),
	JSON5("json5"),
	YAML("yaml"),
	DIFF("diff"),
	CSV("csv"),
	MD("md"),
	SQL("sql"),
	HTML("html"),
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
