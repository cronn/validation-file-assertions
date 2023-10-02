[![CI](https://github.com/cronn/validation-file-assertions/workflows/CI/badge.svg)](https://github.com/cronn/validation-file-assertions/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.cronn/validation-file-assertions/badge.svg)](http://maven-badges.herokuapp.com/maven-central/de.cronn/validation-file-assertions)
[![Apache 2.0](https://img.shields.io/github/license/cronn/validation-file-assertions.svg)](http://www.apache.org/licenses/LICENSE-2.0)
[![Valid Gradle Wrapper](https://github.com/cronn/validation-file-assertions/workflows/Validate%20Gradle%20Wrapper/badge.svg)](https://github.com/cronn/validation-file-assertions/actions/workflows/gradle-wrapper-validation.yml)

# "validation-file-assertions" – File Based Assertions for Java

### Idea shown by example

Let’s consider the following test:

```java
import de.cronn.validationfile.junit5.JUnit5ValidationFileAssertions;

class MyTest implements JUnit5ValidationFileAssertions {
    @Test
    void myTestMethod() {
        assertWithFile("actual value");
    }
}
```

When we run this test for the first time, it will create the following files:
 - `data/test/output/MyTest_myTestMethod.txt` (this one always contains the actual value from the last run; this file should be ignored in your SCM) with
   ```
   actual value
   ```
 - `data/test/validation/MyTest_myTestMethod.txt` (this one is prefilled automatically once during the first test run; on consecutive runs it will not be modified automatically) containing
   ```
   === new file "data/test/validation/MyTest_myTestMethod.txt" ===
   actual value
   ```

The test will fail as long as these two files are different. It’s the developer’s job to manually review the content of the `validation` file and remove the "new file" marker to make the test passing.
The reviewed validation file is then committed to the SCM repository together with the test itself.
Starting from this point `validation` file is the expected value of the test.

Any change to `"actual value"` will cause the test to fail and in this case the developer has to compare the content of `output` file with `validation` and if satisfied copy `output` to `validation` - this makes the test green.

We recommend using a good diffing tool, such as the built-in differ of IntelliJ or [Meld][meld], that allows you to diff the two directories `data/test/actual` vs `data/test/validation`.
We also provide an [IntelliJ plugin][intellij_plugin] that diffs the two directories with one shortcut.

### Quickstart
* Add the dependency

#### Gradle
```gradle
testImplementation 'de.cronn:validation-file-assertions:{version}'
```

#### Maven
```xml
<dependency>
    <groupId>de.cronn</groupId>
    <artifactId>validation-file-assertions</artifactId>
    <version>{version}</version>
    <scope>test</scope>
</dependency>
```

* Configure your SCM

    `.gitignore`
    ```
    data/test/output/
    data/test/tmp/
    ```

* Let your test class implement the `JUnit5ValidationFileAssertions` interface if you are using JUnit5, otherwise use `ValidationFileAssertions`

* Pick suitable `assertWithFile` method and enjoy your first validation file assertion.

### Configurable validation files directory path
* Implement `de.cronn.assertions.validationfile.config.Configure` and override method `getDataDirectory()` with path to desired location

* Register implemented configuration by creating file with fully qualified class name for example:
```
/Resources/META-INF/services/de.cronn.assertions.validationfile.config.Configuration
```

with content

```
com.demo.config.PathConfiguration
```

### See also

* [Intellij plugin for validation file comparison][intellij_plugin]

[meld]: https://meldmerge.org/
[intellij_plugin]: https://plugins.jetbrains.com/plugin/12931-validation-file-comparison
