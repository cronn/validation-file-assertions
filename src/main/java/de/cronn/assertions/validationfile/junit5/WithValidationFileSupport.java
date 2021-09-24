package de.cronn.assertions.validationfile.junit5;

import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Retention;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(ValidationFileSupportExtension.class)
@Retention(RUNTIME)
public @interface WithValidationFileSupport {
	String base() default "data/test";
	boolean reflectPackageStructure() default false;
}
