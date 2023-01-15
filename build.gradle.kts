import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    application
    id("java")
    id("edu.sc.seis.launch4j") version "2.5.4"
}

launch4j {
    mainClassName = "net.loute.freem.compiler.main.frcKt"
    icon = "${projectDir}/icons/myApp.ico"
}

group = "net.loute.freem.compiler"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("net.loute.freem.compiler.main.frcKt")
}