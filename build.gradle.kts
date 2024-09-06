plugins {
    application
    kotlin("jvm") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.serialization") version "2.0.20"
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
    implementation("io.ktor:ktor-client-core-jvm:2.3.12")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation-jvm:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:2.3.12")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.7.2")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.8.1")

    // Use JUnit test framework
    testImplementation("junit:junit:4.12")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
}

kotlin {
    jvmToolchain(17)
}