package de.cronn.configurationtest.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import de.cronn.assertions.validationfile.config.Configuration;

public class TestConfiguration implements Configuration {

	public static final Path PATH = Paths.get("src", "test", "resources", "data");

	@Override
	public Path getDataDirectory() {
		return PATH;
	}
}
