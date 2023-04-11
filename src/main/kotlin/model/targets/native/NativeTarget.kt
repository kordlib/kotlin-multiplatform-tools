package dev.kord.gradle.model.targets.native

import dev.kord.gradle.model.ItemParent
import dev.kord.gradle.model.targets.AbstractTargetItem
import dev.kord.gradle.model.targets.addTarget
import org.gradle.api.Action
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.konan.target.Family
import org.jetbrains.kotlin.konan.target.HostManager

public typealias NativeTargetItem = AbstractTargetItem<out KotlinNativeTarget, KotlinCommonCompilerOptions>

@PublishedApi
internal inline fun <Target : KotlinNativeTarget> ItemParent.addNativeTarget(
    os: Family, name: String,
    crossinline register: KotlinMultiplatformExtension.(String, Action<Target>) -> Unit,
    configure: NativeTargetItem.() -> Unit
) {
    onlyOnTarget(os) {
        addTarget(name, register, configure)
    }
}

@PublishedApi
internal inline fun onlyOnTarget(os: Family, block: () -> Unit) {
    if (HostManager.host.family == os
        || System.getProperty("dev.kord.gradle.all_targets", "false")
            .toBooleanStrict()
    ) {
        block()
    }
}
