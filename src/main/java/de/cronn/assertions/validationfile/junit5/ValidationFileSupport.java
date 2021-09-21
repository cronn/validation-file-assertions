package de.cronn.assertions.validationfile.junit5;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import de.cronn.assertions.validationfile.ValidationFileAssertions;

public class ValidationFileSupport {
	private static final Map<Long, String> currentTestNames = new ConcurrentHashMap<>();

	static void storeTestName(String name) {
		currentTestNames.put(Thread.currentThread().getId(), name);
	}

	static void removeTestName() {
		currentTestNames.remove(Thread.currentThread().getId());
	}

	public static ValidationFileAssertions validationFileAssertions() {
		return () -> currentTestNames.get(Thread.currentThread().getId());
	}
}
