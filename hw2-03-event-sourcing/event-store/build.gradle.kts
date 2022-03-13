plugins {
    kotlin("jvm")
}

group = "ru.vladrus13"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation("org.litote.kmongo:kmongo:4.5.0")
}