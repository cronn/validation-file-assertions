package de.cronn.assertions.validationfile;

import java.util.concurrent.Callable;

import de.cronn.assertions.validationfile.normalization.ValidationNormalizer;
import de.cronn.assertions.validationfile.util.FileBasedComparisonUtils;

public interface ValidationFileAssertions {

	String getTestName();

	default String getValidationFileName(String baseName, FileExtension extension) {
		return baseName + "." + extension.asString();
	}

	default String getValidationFileName(String baseName, String suffix, FileExtension extension) {
		if (suffix == null || suffix.isEmpty()) {
			return getValidationFileName(baseName, extension);
		}
		return getValidationFileName(baseName + "_" + suffix, extension);
	}

	default FailedAssertionHandler failedAssertionHandler() {
		return null;
	}

	default void assertWithFile(String actualOutput, String filename, ValidationNormalizer normalizer) {
		FailedAssertionHandler failedAssertionsCollector = failedAssertionHandler();
		if (failedAssertionsCollector != null) {
			failedAssertionsCollector.executeAndHandleFailedAssertion(() -> {
				FileBasedComparisonUtils.compareActualWithFileHidden(actualOutput, filename, normalizer);
				return null;
			});
		} else {
			FileBasedComparisonUtils.compareActualWithFileHidden(actualOutput, filename, normalizer);
		}
	}

	default void assertWithFile(String actualString, FileExtension extension) {
		assertWithFile(actualString, getValidationFileName(getTestName(), extension), null);
	}

	default void assertWithFileWithSuffix(String actualString, String suffix, FileExtension extension) {
		assertWithFileWithSuffix(actualString, null, suffix, extension);
	}

	default void assertWithFileWithSuffix(String actualString, ValidationNormalizer validationNormalizer, String suffix, FileExtension extension) {
		assertWithFile(actualString, getValidationFileName(getTestName(), suffix, extension), validationNormalizer);
	}

	default void assertWithFile(String actualOutput, ValidationNormalizer validationNormalizer, FileExtension extension) {
		assertWithFile(actualOutput, getValidationFileName(getTestName(), extension), validationNormalizer);
	}

	default void assertWithFile(String actualString) {
		assertWithFile(actualString, FileExtensions.TXT);
	}

	default void assertWithFileWithSuffix(String actualString, String suffix) {
		assertWithFileWithSuffix(actualString, suffix, FileExtensions.TXT);
	}

	default void assertWithFileWithSuffix(String actualString, ValidationNormalizer validationNormalizer, String suffix) {
		assertWithFileWithSuffix(actualString, validationNormalizer, suffix, FileExtensions.TXT);
	}

	default void assertWithFile(String actualOutput, ValidationNormalizer validationNormalizer) {
		assertWithFile(actualOutput, validationNormalizer, FileExtensions.TXT);
	}

	default void assertWithJsonFile(String actualJsonString) {
		assertWithFile(actualJsonString, FileExtensions.JSON);
	}

	default void assertWithJsonFileWithSuffix(String actualJsonString, String suffix) {
		assertWithFileWithSuffix(actualJsonString, suffix, FileExtensions.JSON);
	}

	default void assertWithJsonFileWithSuffix(String actualJsonString, ValidationNormalizer validationNormalizer, String suffix) {
		assertWithFileWithSuffix(actualJsonString, validationNormalizer, suffix, FileExtensions.JSON);
	}

	default void assertWithJsonFile(String actualJsonString, ValidationNormalizer validationNormalizer) {
		assertWithFile(actualJsonString, validationNormalizer, FileExtensions.JSON);
	}

	default void assertWithXmlFile(String actualXmlString) {
		assertWithFile(actualXmlString, FileExtensions.XML);
	}

	default void assertWithXmlFileWithSuffix(String actualXmlString, String suffix) {
		assertWithFileWithSuffix(actualXmlString, suffix, FileExtensions.XML);
	}

	default void assertWithXmlFileWithSuffix(String actualXmlString, ValidationNormalizer validationNormalizer, String suffix) {
		assertWithFileWithSuffix(actualXmlString, validationNormalizer, suffix, FileExtensions.XML);
	}

	default void assertWithXmlFile(String actualXmlString, ValidationNormalizer validationNormalizer) {
		assertWithFile(actualXmlString, validationNormalizer, FileExtensions.XML);
	}

	interface FailedAssertionHandler {
		void executeAndHandleFailedAssertion(Callable<Void> callable);
	}

}
