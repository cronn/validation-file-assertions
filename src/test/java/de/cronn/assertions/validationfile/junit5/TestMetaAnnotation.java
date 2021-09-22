package de.cronn.assertions.validationfile.junit5;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;

import org.junit.jupiter.api.Tag;

@WithValidationFileSupport(base = "build/config-dir-from-meta")
@Tag("unit")
@Retention(RUNTIME)
public @interface TestMetaAnnotation {

}

