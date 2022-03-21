plugins {
    kotlin("jvm")
    application
    kotlin("plugin.serialization")
}

dependencies {
    implementation("io.ktor:ktor-html-builder:1.6.7")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.251-kotlin-1.5.31")
    implementation("io.ktor:ktor-server-sessions:1.6.7")
    implementation("io.ktor:ktor-auth:1.6.7")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.3.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
}

repositories {
    mavenCentral()
}