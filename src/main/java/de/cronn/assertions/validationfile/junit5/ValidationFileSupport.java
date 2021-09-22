package de.cronn.assertions.validationfile.junit5;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.cronn.assertions.validationfile.ValidationFileAssertions;

public class ValidationFileSupport {
	private static final Map<Long, ValidationFileAssertions> assertionsPerThread = new ConcurrentHashMap<>();

	static void storeAssertions(ValidationFileAssertions assertions) {
		assertionsPerThread.put(Thread.currentThread().getId(), assertions);
	}

	static void removeAssertions() {
		assertionsPerThread.remove(Thread.currentThread().getId());
	}

	public static ValidationFileAssertions validationFileAssertions() {
		ValidationFileAssertions assertions = assertionsPerThread.get(Thread.currentThread().getId());
		if (assertions==null) {
			throw new IllegalStateException("No assertions for test run. " +
				"Didn't you forgot to annotate with @WithValidationFileSupport or nested/dynamic testcase.");
		}
		return assertions;
	}
}
