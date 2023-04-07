package dev.kord.gradle.model.targets.native

import dev.kord.gradle.model.ItemParent
import dev.kord.gradle.model.targets.addTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public inline fun ItemParent.mingwX64(name: String = "mingwX64", configure: NativeTargetItem.() -> Unit = {}) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    addTarget(name, KotlinMultiplatformExtension::mingwX64, configure)
}
