package de.cronn.assertions.validationfile.sample;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.platform.commons.util.ReflectionUtils;

final class DummySerializer {

	private DummySerializer() {
	}

	static String toCsvString(Object obj) {
		Map<String, Object> properties = properties(obj);
		String header = String.join(",", properties.keySet());
		String values = properties.values().stream()
			.map(String::valueOf)
			.collect(Collectors.joining(","));
		return header + "\n" + values;
	}

	static String toJsonString(Object obj) {
		return properties(obj).entrySet().stream()
			.map(entry -> String.format("\"%s\" : \"%s\"", entry.getKey(), entry.getValue()))
			.collect(Collectors.joining(",\n", "{\n", "\n}"));
	}

	static String toXmlString(Object obj) {
		String className = obj.getClass().getSimpleName();
		return properties(obj).entrySet().stream()
			.map(entry -> String.format("<%1$s>%2$s</%1$s>", entry.getKey(), entry.getValue()))
			.collect(Collectors.joining("\n", "<" + className + ">\n", "\n</" + className + ">"));
	}

	private static Map<String, Object> properties(Object obj) {
		return ReflectionUtils.findMethods(obj.getClass(), method -> method.getName().startsWith("get"))
			.stream()
			.collect(Collectors
				.toMap(method -> method.getName().substring(3), method -> ReflectionUtils.invokeMethod(method, obj),
					(a, b) -> a,
					LinkedHashMap::new));
	}
}
