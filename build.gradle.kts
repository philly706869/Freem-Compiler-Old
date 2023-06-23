plugins {
    kotlin("jvm") version "1.9.0-RC"
}

group = "net.loute.freem.compiler"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.json:json:20230618")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}