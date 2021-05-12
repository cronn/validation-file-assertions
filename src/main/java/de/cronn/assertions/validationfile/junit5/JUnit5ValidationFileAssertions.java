package de.cronn.assertions.validationfile.junit5;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

import de.cronn.assertions.validationfile.ValidationFileAssertions;
import de.cronn.assertions.validationfile.util.TestNameUtils;

/**
 * <p>
 * Provides <i>test name</i> as default name of validation file.<br/>
 * This interface is an alternative for storing {@link TestInfo} in class field.
 *
 * <p>
 * Notes:
 * <ul>
 * <li>Parallel test execution <i>ExecutionMode.CONCURRENT</i> combined with <i>TestInstance.Lifecycle.PER_CLASS</i> is not supported.</li>
 * </ul>
 *
 * <p>
 * Example:<br/>
 * <i>MyTestClass.java</i>:
 * <pre><code class='java'>
 * class MyTestClass implements JUnit5ValidationFileAssertions {
 *   &#064;Test
 *   void myTestMethod() {
 *     compareActualWithFile(&quot;this will be content of 'MyTestClass_myTestMethod.txt'&quot;);
 *   }
 * }
 * </code></pre>
 */
public interface JUnit5ValidationFileAssertions extends ValidationFileAssertions {

	@BeforeEach
	default void storeTestInfoOfTestBeingExecuted(TestInfo testInfo) {
		TestInfoStore.store(this, testInfo);
	}

	@AfterEach
	default void discardTestInfoOfTestBeingExecuted() {
		TestInfoStore.discard(this);
	}

	@Override
	default String getTestName() {
		Class<?> testClass = TestInfoStore.getTestClass(this);
		String testMethodName = TestInfoStore.getTestMethodName(this);
		return TestNameUtils.getTestName(testClass, testMethodName);
	}

}
