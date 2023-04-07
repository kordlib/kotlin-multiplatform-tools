package dev.kord.gradle.model

import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public typealias DependencyConfigurator = KotlinDependencyHandler.() -> Unit

public interface HasDependencies : DependencyHandler
public interface HasTestDependencies : HasDependencies {
    public val testDependencyHandler: DependencyHandler
}

public inline fun HasTestDependencies.testDependencies(configure: DependencyHandler.() -> Unit) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    testDependencyHandler.apply(configure)
}


public interface DependencyHandler {
    public var dependencyConfigurator: DependencyConfigurator?

    public fun dependencies(configurator: DependencyConfigurator) {
        dependencyConfigurator = configurator
    }
}
