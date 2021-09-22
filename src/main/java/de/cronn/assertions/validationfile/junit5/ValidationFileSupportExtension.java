package de.cronn.assertions.validationfile.junit5;

import static de.cronn.assertions.validationfile.junit5.ValidationFileSupport.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import de.cronn.assertions.validationfile.DirsConfig;
import de.cronn.assertions.validationfile.ValidationFileAssertions;

class ValidationFileSupportExtension implements InvocationInterceptor {
	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		String testName = extensionContext.getTestClass().map(Class::getSimpleName).orElse("UNKNOWN") + "_"
			+ extensionContext.getTestMethod().map(Method::getName).orElse("UNKNOWN");

		DirsConfig dirsConfig = invocationContext.getTarget()
			.map(Object::getClass)
			.flatMap(this::findAnnotation)
			.map(WithValidationFileSupport::base)
			.map(Paths::get)
			.map(p -> (DirsConfig) () -> p)
			.orElse(DirsConfig.DEFAULT);

		storeAssertions(new ValidationFileAssertions() {
			@Override
			public String getTestName() {
				return testName;
			}

			@Override
			public DirsConfig dirsConfig() {
				return dirsConfig;
			}
		});
		InvocationInterceptor.super.interceptTestMethod(invocation, invocationContext, extensionContext);
		removeAssertions();
	}

	private Optional<WithValidationFileSupport> findAnnotation(Class<?> aClass) {
		Optional<WithValidationFileSupport> annotation = Optional.ofNullable(aClass.getAnnotation(WithValidationFileSupport.class));
		if (!annotation.isPresent()) {
			annotation = Stream.of(aClass.getAnnotations())
				.map(Annotation::annotationType)
				.flatMap(a -> findAnnotation(a).map(Stream::of).orElse(Stream.empty()))
				.findFirst();
		}
		return annotation;
	}

	@Override
	public void interceptDynamicTest(Invocation<Void> invocation, ExtensionContext extensionContext) throws Throwable {
		System.err.println("WARNING: ValidationFileSupport.getTestName() is not supported for dynamic tests.");
		InvocationInterceptor.super.interceptDynamicTest(invocation, extensionContext);
	}
}
