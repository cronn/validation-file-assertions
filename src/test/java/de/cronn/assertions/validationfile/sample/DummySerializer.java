package de.cronn.assertions.validationfile.sample;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.platform.commons.util.ReflectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

final class DummySerializer {

	static ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule())
		.setDateFormat(new StdDateFormat())
		.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);

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

	static String toJsonString(Object obj) throws IOException {
		return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
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
