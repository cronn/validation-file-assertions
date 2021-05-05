package de.cronn.assertions.validationfile.junit5;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.jupiter.api.TestInfo;

final class TestInfoStore {

	private static final Map<Object, SimpleTestInfo> testInfos = new ConcurrentHashMap<>();

	private TestInfoStore() {
	}

	static void store(Object testInstance, TestInfo testInfo) {
		Class<?> testClass = testInfo.getTestClass().orElseThrow(() -> new IllegalStateException("No test class"));
		Method testMethod = testInfo.getTestMethod().orElseThrow(() -> new IllegalStateException("No test method"));
		testInfos.put(testInstance, new SimpleTestInfo(testClass, testMethod.getName()));
	}

	static void discard(Object testInstance) {
		testInfos.remove(testInstance);
	}

	static Class<?> getTestClass(Object testInstance) {
		return testInfos.get(testInstance).testClass;
	}

	static String getTestMethodName(Object testInstance) {
		return testInfos.get(testInstance).methodName;
	}

	private static final class SimpleTestInfo {
		final Class<?> testClass;
		final String methodName;

		private SimpleTestInfo(Class<?> testClass, String methodName) {
			this.testClass = testClass;
			this.methodName = methodName;
		}
	}
}
