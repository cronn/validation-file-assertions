plugins {
    java
}

group = "de.cronn"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
    withJavadocJar()
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":"))

    testImplementation("org.junit.jupiter:junit-jupiter:[5.0,6.0)")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.assertj:assertj-core:latest.release")

    components.all {
        if (id.version.matches(Regex("(?i).+[-.](CANDIDATE|RC|BETA|ALPHA|M\\d+).*"))) {
            status = "milestone"
        }
    }
}

tasks.test {
    useJUnitPlatform()
}

dependencyLocking {
    lockAllConfigurations()
}
