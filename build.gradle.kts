import Build_gradle.Versions.clikt
import org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.30"

    application
}

repositories {
    jcenter()
    mavenCentral()
}

object Versions {
    const val junit = "5.7.0"
    const val atrium = "0.15.0"
    const val kotlinCoroutines = "1.4.2"
    const val clikt = "3.1.0"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")
    implementation("com.github.ajalt.clikt:clikt:${clikt}")

    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:${Versions.junit}")
    testImplementation("ch.tutteli.atrium:atrium-fluent-en_GB:${Versions.atrium}")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinCoroutines}")
}

val kotlinOptions: KotlinJvmOptions.() -> Unit = {
    jvmTarget = "11"
    freeCompilerArgs = listOf("-Xinline-classes")

}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions(kotlinOptions)
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions(kotlinOptions)

tasks.test {
    useJUnitPlatform()
    testLogging {
        lifecycle {
            events = setOf(FAILED, PASSED, SKIPPED)
            exceptionFormat = FULL
            showExceptions = true
            showCauses = true
            showStackTraces = true
            showStandardStreams = true
        }
    }
}

task("runConnectedSourceHosts", JavaExec::class) {
    classpath = sourceSets.main.get().runtimeClasspath

    mainClass.set("main.ConnectedSourceHostsKt")
}

task("runPeriodicReports", JavaExec::class) {
    classpath = sourceSets.main.get().runtimeClasspath

    mainClass.set("main.PeriodicReportsKt")
}
