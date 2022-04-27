package de.cronn.assertions.validationfile.junit5;

import static de.cronn.assertions.validationfile.junit5.ValidationFileSupport.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.nio.file.Path;
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
		Optional<Class<?>> testClassIfProvided = extensionContext.getTestClass();

		String testName = testClassIfProvided.map(Class::getSimpleName).orElse("UNKNOWN") + "_"
			+ extensionContext.getTestMethod().map(Method::getName).orElse("UNKNOWN");

		Optional<DirsConfig> configFromAnnotation = invocationContext.getTarget()
			.map(Object::getClass)
			.flatMap(this::findAnnotation)
			.map(a -> new DirsConfig() {
				@Override
				public Path base() {
					return Paths.get(a.base());
				}

				@Override
				public Path relative() {
					Optional<Path> p = Optional.empty();
					if (a.reflectPackageStructure()) {
						 p = testClassIfProvided
							.map(Class::getPackage)
							.map(aPackage -> Paths.get("", aPackage.getName().split("\\.")));
					}
					return p.orElse(DirsConfig.super.relative());
				}
			});

		DirsConfig dirsConfig = configFromAnnotation
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
