import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20-RC"
    application
    kotlin("plugin.serialization") version "1.4.21"
}

group = "ru.vladrus13"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.testcontainers:testcontainers:1.16.3")
    testImplementation("org.testcontainers:junit-jupiter:1.16.3")
    testImplementation("io.ktor:ktor-server-tests:1.6.7")
    testImplementation("io.ktor:ktor-server-netty:1.6.7")
    testImplementation("io.ktor:ktor-server-core:1.6.7")
    testImplementation("io.ktor:ktor-client-apache:1.6.7")
    testImplementation("io.ktor:ktor-client-jackson:1.6.7")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    testImplementation(kotlin("test"))
    implementation(project("system"))
    implementation(project("users"))
    implementation(project("common"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("ru.vladrus13.system.ApplicationKt")
}