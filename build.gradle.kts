plugins {
    application
    kotlin("jvm") version "1.9.23"
}

group = "com.bbm"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.harper.asteroids.App")
}

repositories {
    mavenCentral()
}

dependencies {
    // This dependency is used by the application.
    implementation("com.google.guava:guava:28.0-jre")

    implementation("com.fasterxml.jackson.core:jackson-annotations:2.9.5")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:2.27")
    implementation("javax.ws.rs:javax.ws.rs-api:2.1")
    implementation("javax.activation:javax.activation-api:1.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.3.12")

    // Use JUnit test framework
    testImplementation("junit:junit:4.12")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
}

kotlin {
    jvmToolchain(17)
}