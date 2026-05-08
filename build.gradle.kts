buildscript {
    configurations.classpath {
        resolutionStrategy {
            // Fix https://github.com/jreleaser/jreleaser/issues/1643
            force("org.eclipse.jgit:org.eclipse.jgit:5.13.0.202109080827-r")
        }
    }
}

plugins {
    `java-library`
    `maven-publish`
    signing
    id("com.diffplug.spotless") version "latest.release"
    id("org.jreleaser") version "1.24.0"
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    compileOnly("org.junit.jupiter:junit-jupiter-api:[5.0,6.0)")
    api("com.googlecode.java-diff-utils:diffutils:latest.release")
    api("org.opentest4j:opentest4j:latest.release")

    testImplementation("org.junit.jupiter:junit-jupiter:[5.0,6.0)")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:latest.release")
    testImplementation("com.fasterxml.jackson.core:jackson-databind")
    testImplementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    testImplementation(platform("com.fasterxml.jackson:jackson-bom:latest.release"))

    components.all {
        if (id.version.matches(Regex("(?i).+[-.](CANDIDATE|RC|BETA|ALPHA|M\\d+).*"))) {
            status = "milestone"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.javadoc {
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

group = "de.cronn"
version = System.getenv("ARTIFACT_VERSION") ?: "1.0.0-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            pom {
                name = project.name
                description = "File Based Assertions for Java"
                url = "https://github.com/cronn/validation-file-assertions"

                licenses {
                    license {
                        name = "The Apache Software License, Version 2.0"
                        url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                    }
                }

                developers {
                    developer {
                        name = "Radosław Postołowicz"
                        email = "radoslaw.postolowicz@cronn.de"
                    }
                }

                scm {
                    url = "https://github.com/cronn/validation-file-assertions"
                }

                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
            }
        }
    }
    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("staging-deploy"))
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

jreleaser {
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    sign = false
                    setActive("RELEASE")
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository("build/staging-deploy")
                }
            }
        }
    }
}

spotless {
    java {
        googleJavaFormat()
        removeUnusedImports()
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        ktlint()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.wrapper {
    gradleVersion = "9.5.0"
    distributionType = Wrapper.DistributionType.ALL
}

dependencyLocking {
    lockAllConfigurations()
}
