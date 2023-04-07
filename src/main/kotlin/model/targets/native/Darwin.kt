package dev.kord.gradle.model.targets.native

import dev.kord.gradle.model.ItemParent
import dev.kord.gradle.model.group
import dev.kord.gradle.model.targets.addTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public inline fun ItemParent.ios(name: String = "ios", configure: NativeTargetItem.() -> Unit = {}) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    addTarget(name, KotlinMultiplatformExtension::ios, configure)
}

public inline fun ItemParent.tvos(name: String = "tvos", configure: NativeTargetItem.() -> Unit = {}) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    addTarget(name, KotlinMultiplatformExtension::tvos, configure)
}

public inline fun ItemParent.watchos(name: String = "watchos", configure: NativeTargetItem.() -> Unit = {}) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    addTarget(name, KotlinMultiplatformExtension::watchos, configure)
}

public inline fun ItemParent.macosX64(name: String = "macosX64", configure: NativeTargetItem.() -> Unit = {}) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    addTarget(name, KotlinMultiplatformExtension::macosX64, configure)
}

public inline fun ItemParent.macosArm64(name: String = "macosArm64", configure: NativeTargetItem.() -> Unit = {}) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    addTarget(name, KotlinMultiplatformExtension::macosArm64, configure)
}

public inline fun ItemParent.macos(name: String = "macos", configure: NativeTargetItem.() -> Unit = {}) {
    contract {
        callsInPlace(configure, InvocationKind.AT_LEAST_ONCE)
    }

    group(name) {
        macosArm64("${name}Arm64", configure)
        macosX64("${name}X64", configure)
    }
}

public inline fun ItemParent.darwin(name: String = "darwin", configure: NativeTargetItem.() -> Unit = {}) {
    contract {
        callsInPlace(configure, InvocationKind.AT_LEAST_ONCE)
    }

    group(name) {
        macos(configure = configure)
        tvos(configure = configure)
        watchos(configure = configure)
        ios(configure = configure)
    }
}
