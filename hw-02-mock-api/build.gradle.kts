import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.30"
}

group = "ru.vladrus13"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.21")
    implementation("com.vk.api:sdk:1.0.11")
    implementation("org.slf4j:slf4j-jdk14:1.7.32")
    implementation("org.jsoup:jsoup:1.14.2")
    implementation("org.json:json:20210307")
    testImplementation("com.xebialabs.restito:restito:0.9.4")

}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}