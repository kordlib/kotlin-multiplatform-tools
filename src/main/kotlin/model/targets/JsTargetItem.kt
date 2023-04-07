package dev.kord.gradle.model.targets

import dev.kord.gradle.model.ItemParent
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompilerOptions
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public typealias JsTargetItem = AbstractTargetItem<KotlinJsTargetDsl, KotlinJsCompilerOptions>

public inline fun ItemParent.js(
    name: String = "js",
    configure: JsTargetItem.() -> Unit = {}
) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    addTarget(
        name,
        { actualName, actualConfigure -> js(actualName, KotlinJsCompilerType.IR, actualConfigure) },
        configure
    )
}

public inline fun ItemParent.nodejs(
    name: String = "js",
    configure: JsTargetItem.() -> Unit = {}
) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    return js(name) {
        target {
            nodejs()
        }
    }
}


public inline fun ItemParent.fullJs(
    name: String = "js",
    configure: JsTargetItem.() -> Unit = {}
) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    return js(name) {
        target {
            browser()
            nodejs()
        }
    }
}

