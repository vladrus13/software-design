import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.bmuschko:gradle-docker-plugin:7.3.0")
    }
}

apply(plugin = "com.bmuschko.docker-remote-api")

plugins {
    kotlin("jvm")
    application
    id("com.bmuschko.docker-remote-api") version "7.3.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(project(":common"))
    implementation("io.ktor:ktor-server-core:1.6.7")
    implementation("io.ktor:ktor-server-netty:1.6.7")
    implementation("io.ktor:ktor-html-builder:1.6.7")
    testImplementation("io.ktor:ktor-server-tests:1.6.7")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.251-kotlin-1.5.31")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2")
}

tasks {
    jar {
        enabled = true
        manifest {
            attributes["Main-Class"] = "ru.vladrus13.system.ApplicationKt"
        }
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    }
}

tasks.create("copyFat", Copy::class) {
    dependsOn("jar")
    from("build/libs/system.jar")
    into("build/docker")
}

task<Dockerfile>("create") {
    dependsOn("copyFat")
    from("openjdk:8")
    addFile("system.jar", "/app/system.jar")
    exposePort(8080)
    defaultCommand("java", "-jar", "/app/system.jar")
}

tasks.create("buildT", DockerBuildImage::class) {
    dependsOn("clean", "create")
    images.add("system:latest")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}