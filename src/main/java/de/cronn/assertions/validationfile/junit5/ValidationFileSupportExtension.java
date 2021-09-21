package de.cronn.assertions.validationfile.junit5;

import static de.cronn.assertions.validationfile.junit5.ValidationFileSupport.*;

import java.lang.reflect.Method;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

class ValidationFileSupportExtension implements InvocationInterceptor {
	@Override
	public void interceptTestMethod(Invocation<Void> invocation, ReflectiveInvocationContext<Method> invocationContext, ExtensionContext extensionContext) throws Throwable {
		String testName = extensionContext.getTestClass().map(Class::getSimpleName).orElse("UNKNOWN") + "_"
			+ extensionContext.getTestMethod().map(Method::getName).orElse("UNKNOWN");

		storeTestName(testName);
		InvocationInterceptor.super.interceptTestMethod(invocation, invocationContext, extensionContext);
		removeTestName();
	}

	@Override
	public void interceptDynamicTest(Invocation<Void> invocation, ExtensionContext extensionContext) throws Throwable {
		System.err.println("WARNING: ValidationFileSupport.getTestName() is not supported for dynamic tests.");
		InvocationInterceptor.super.interceptDynamicTest(invocation, extensionContext);
	}
}
