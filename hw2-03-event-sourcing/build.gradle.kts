import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20-RC"
    application
}

group = "ru.vladrus13"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project("common"))
    implementation(project("manager-admin"))
    implementation(project("event-store"))
    implementation(project("report-service"))
    implementation(project("turnstile"))
    implementation("org.litote.kmongo:kmongo:4.5.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}