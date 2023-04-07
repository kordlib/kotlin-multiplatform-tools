package dev.kord.gradle

import dev.kord.gradle.model.HasSourceSets

internal val HasSourceSets.mainSourceSet: String
    get() = "${name}Main"

internal val HasSourceSets.testSourceSet: String
    get() = "${name}Test"

internal val HasSourceSets.testTask: String
    get() = "${name}Test"

internal val HasSourceSets.compileKotlinTask: String
    get() = "compileKotlin${name.capitalize()}"

internal val HasSourceSets.compileTestKotlinTask: String
    get() = "compileTestKotlin${name.capitalize()}"

internal val HasSourceSets.publishToMavenLocalTask: String
    get() = "publish${name.capitalize()}PublicationToMavenLocal"

internal val HasSourceSets.publishToMavenRepositoryTask: String
    get() = "publish${name.capitalize()}PublicationToMavenRepository"

private fun String.capitalize() = first().uppercase() + drop(1)
