plugins {
    application
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.10"
}

group = "com.bbm"

version = "1.0-SNAPSHOT"

application { mainClass.set("com.harper.asteroids.AppKt") }

repositories { mavenCentral() }

dependencies {
    // This dependency is used by the application.
    implementation("io.ktor:ktor-client-core:2.3.13")
    implementation("io.ktor:ktor-client-cio:2.3.13")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.13")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.13")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    // Use JUnit test framework
    testImplementation("junit:junit:4.13.1")
    testImplementation("org.hamcrest:hamcrest-library:1.3")
}

kotlin { jvmToolchain(17) }
