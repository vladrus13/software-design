import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.20-M1"
    application
}

group = "ru.vladrus13"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex:rxnetty-http:0.5.3")
    implementation("io.reactivex:rxnetty-common:0.5.3")
    implementation("io.reactivex:rxnetty-tcp:0.5.3")
    implementation("io.netty:netty-all:4.1.72.Final")
    implementation("org.mongodb:mongodb-driver-rx:1.5.0")
    implementation("io.ktor:ktor-html-builder:1.6.7")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

application {
    mainClass.set("MainKt")
}