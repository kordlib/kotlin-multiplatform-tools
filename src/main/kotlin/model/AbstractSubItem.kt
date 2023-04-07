package dev.kord.gradle.model

import dev.kord.gradle.Dsl
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

@Dsl
public abstract class AbstractSubItem internal constructor() :
    SubItem, HasTestDependencies, HasSourceSets {
    override var dependencyConfigurator: DependencyConfigurator? = null
    override val testDependencyHandler: DependencyHandler = TestDependencyHandler()

    protected fun KotlinMultiplatformExtension.applyDependencyHandlers() {
        sourceSets.apply {
            getByName("${name}Main").applyDependencyHandler(this@AbstractSubItem)
            getByName("${name}Test").applyDependencyHandler(testDependencyHandler)
        }
    }
}

private class TestDependencyHandler : DependencyHandler {
    override var dependencyConfigurator: DependencyConfigurator? = null
}

private fun KotlinSourceSet.applyDependencyHandler(dependencyHandler: DependencyHandler) {
    println("Applying dependencies for: $name")
    if (dependencyHandler.dependencyConfigurator != null) {
        println("Applying dependencies for: ${dependencyHandler.dependencyConfigurator}")
        dependencies {
            dependencyHandler.dependencyConfigurator?.invoke(this)
        }
    }
}
