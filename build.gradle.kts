@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `groovy-gradle-plugin`
    id("com.gradle.plugin-publish") version "1.1.0"
}

group = "dev.kord"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(kotlin("gradle-plugin", "1.8.20"))
}

tasks {
    withType<KotlinCompile> {
        compilerOptions {
            freeCompilerArgs.addAll("-opt-in=dev.kord.gradle.Internal", "-opt-in=kotlin.contracts.ExperimentalContracts", "-Xcontext-receivers")
        }
    }
}

gradlePlugin {
    plugins {
        create("kotlin-multiplatform.plugin") {
            id = "dev.kord.kotlin-multiplatform-plugin"
            implementationClass = "dev.kord.gradle.KotlinMultiplatformPlugin"
            displayName = "Kotlin Multiplatform helper"
            description = "Simplifies the creation of Kotlin multiplatform projects"
            tags.set(setOf("kotlin", "multiplatform"))
        }
    }

    website.set("https://github.com/kordlib/multiplatform-gradle-plugin")
    vcsUrl.set("https://github.com/kordlib/multiplatform-gradle-plugin")

}

kotlin {
    explicitApi()
    jvmToolchain(8)
}
