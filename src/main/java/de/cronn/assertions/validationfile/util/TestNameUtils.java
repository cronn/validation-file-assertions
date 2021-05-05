package de.cronn.assertions.validationfile.util;

public final class TestNameUtils {

	private TestNameUtils() {
	}

	public static String getTestName(Class<?> aClass, String methodName) {
		return join(enclosingClassesUpstream(aClass), methodName);
	}

	private static String enclosingClassesUpstream(Class<?> aClass) {
		String classHierarchy = aClass.getSimpleName();
		Class<?> enclosingClass = aClass.getEnclosingClass();
		while (enclosingClass != null) {
			classHierarchy = join(enclosingClass.getSimpleName(), classHierarchy);
			enclosingClass = enclosingClass.getEnclosingClass();
		}
		return classHierarchy;
	}

	private static String join(String element, String other) {
		return other.startsWith("_") ? (element + other) : (element + "_" + other);
	}
}
