package dev.kord.gradle.model.targets

import dev.kord.gradle.Dsl
import dev.kord.gradle.model.AbstractSubItem
import dev.kord.gradle.model.ItemParent
import org.gradle.api.Action
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget

@PublishedApi
internal inline fun <Target : KotlinTarget,
        CompilerOptions : KotlinCommonCompilerOptions> ItemParent.addTarget(
    name: String,
    crossinline register: KotlinMultiplatformExtension.(String, Action<Target>) -> Unit,
    configure: AbstractTargetItem<Target, CompilerOptions>.() -> Unit
) {
    val implementation = object : AbstractTargetItem<Target, CompilerOptions>() {
        override val name: String
            get() = name

        override fun KotlinMultiplatformExtension.configure(configure: Target.() -> Unit) = register(name, Action {
            configure()
        })

    }

    addItem(implementation.apply(configure))
}

@Dsl
public abstract class AbstractTargetItem<Target : KotlinTarget,
        CompilerOptions : KotlinCommonCompilerOptions> @PublishedApi internal constructor() : AbstractSubItem() {
    public var compilerConfigurator: (CompilerOptions.() -> Unit)? = null
    public var targetConfigurator: (Target.() -> Unit)? = null

    public fun kotlinCompile(configurator: CompilerOptions.() -> Unit) {
        compilerConfigurator = configurator
    }

    public fun target(configurator: Target.() -> Unit) {
        targetConfigurator = configurator
    }

    protected abstract fun KotlinMultiplatformExtension.configure(configure: Target.() -> Unit)

    context(Project)
    @Suppress("UNCHECKED_CAST")
    override fun KotlinMultiplatformExtension.apply() {
        configure {
            targetConfigurator?.invoke(this)
            compilations.all {
                compilerOptions.options.apply {
                    compilerConfigurator?.let { (this as CompilerOptions).it() }
                }
            }
        }
        applyDependencyHandlers()
    }
}
