package dev.kord.gradle.model.targets

import dev.kord.gradle.model.ItemParent
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public typealias JvmTargetItem = AbstractTargetItem<KotlinJvmTarget, KotlinJvmCompilerOptions>

public inline fun ItemParent.jvm(name: String = "jvm", configure: JvmTargetItem.() -> Unit = {}) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    addTarget(name, KotlinMultiplatformExtension::jvm, configure)
}
