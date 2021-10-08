plugins {
    application
    kotlin("jvm") version "1.5.31"
}

group = "ru.vladrus13"
version = "0.0.1"
application {
    mainClass.set("ru.vladrus13.ApplicationKt")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven") }
}

dependencies {
    implementation("io.ktor:ktor-server-core:1.6.4")
    implementation("io.ktor:ktor-server-sessions:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("io.ktor:ktor-html-builder:1.6.4")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-css:1.0.0-pre.254-kotlin-1.5.31")
    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.129-kotlin-1.4.20")
    implementation("io.ktor:ktor-server-netty:1.6.4")
    implementation("ch.qos.logback:logback-classic:1.2.6")
    testImplementation("io.ktor:ktor-server-tests:1.6.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.5.31")
    implementation("org.jetbrains.exposed:exposed:0.17.14")
    implementation("mysql:mysql-connector-java:8.0.25")
}