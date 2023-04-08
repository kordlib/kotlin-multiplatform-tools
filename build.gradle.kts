@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `groovy-gradle-plugin`
    `maven-publish`
    id("com.jfrog.artifactory") version "4.31.8"
}

group = "dev.kord"
version = "1.0.0-SNAPSHOT"

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

artifactory {
    setContextUrl("https://kord.jfrog.io/artifactory")
    publish {
        repository {
            setRepoKey("gradle-dev-local")
            setUsername(System.getenv("JFROG_USER"))
            setPassword(System.getenv("JFROG_PASSWORD"))
            setMavenCompatible(true)

        }

        defaults {
            publications("kotlin-multiplatform.pluginPluginMarkerMaven", "pluginMaven")
        }
    }
    resolve {
        repository {
            setRepoKey("gradle-dev")
            setUsername(System.getenv("JFROG_USER"))
            setPassword(System.getenv("JFROG_PASSWORD"))
            setMavenCompatible(true)
        }
    }
}
