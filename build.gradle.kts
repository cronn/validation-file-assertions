plugins {
    `java-library`
    `maven-publish`
    signing
}

repositories {
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
    withJavadocJar()

    registerFeature("junit5Support") {
        usingSourceSet(sourceSets["main"])
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

dependencies {
    "junit5SupportImplementation"("org.junit.jupiter:junit-jupiter-api:latest.release")
    api("com.googlecode.java-diff-utils:diffutils:latest.release")
    api("org.opentest4j:opentest4j:latest.release")

    testImplementation("org.junit.jupiter:junit-jupiter:latest.release")
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
version = System.getenv("ARTIFACT_VERSION") ?: "SNAPSHOT"

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
            url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2")
            credentials {
                username = if (project.hasProperty("nexusUsername")) project.property("nexusUsername").toString() else System.getenv("NEXUS_USERNAME")
                password = if (project.hasProperty("nexusPassword")) project.property("nexusPassword").toString() else System.getenv("NEXUS_PASSWORD")
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}

tasks.wrapper {
    gradleVersion = "8.10.2"
    distributionType = Wrapper.DistributionType.ALL
}

dependencyLocking {
    lockAllConfigurations()
}
