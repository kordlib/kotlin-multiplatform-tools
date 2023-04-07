package dev.kord.gradle.model

import dev.kord.gradle.Dsl
import dev.kord.gradle.Internal
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.kpm.external.ExternalVariantApi
import org.jetbrains.kotlin.gradle.kpm.external.project
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

@Dsl
public interface HasSourceSets {
    public val name: String
}

@Dsl
public interface ItemParent {
    public val parent: HasSourceSets
    public fun addItem(subItem: SubItem)
}

@Dsl
public interface SubItem {
    context(Project)
    @Internal
    public fun KotlinMultiplatformExtension.apply()
}

@OptIn(ExternalVariantApi::class)
public inline fun KotlinMultiplatformExtension.configureTargets(configure: ItemParent.() -> Unit) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }

    val common = object : HasSourceSets {
        override val name: String = "common"
    }

    val parent = object : ItemParent {
        override val parent: HasSourceSets = common
        override fun addItem(subItem: SubItem) {
            with(project) {
                with(subItem) { apply() }
            }
        }
    }

    parent.configure()
}
