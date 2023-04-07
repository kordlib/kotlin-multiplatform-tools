package dev.kord.gradle.model.targets

import dev.kord.gradle.model.ItemParent
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.kpm.external.ExternalVariantApi
import org.jetbrains.kotlin.gradle.kpm.external.project
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTargetPreset
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public class JsTargetItem(override val name: String) :
    AbstractTargetItem<KotlinJsTargetDsl, KotlinJsCompilerOptions>() {
    @OptIn(ExternalVariantApi::class)
    override fun KotlinMultiplatformExtension.configure(configure: KotlinJsTargetDsl.() -> Unit) {
        val target = LenientJsPreset(project).createTarget(name)
        targets.add(target)
    }
}

public inline fun ItemParent.js(
    name: String = "js",
    configure: JsTargetItem.() -> Unit = {}
) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }
    addItem(JsTargetItem(name).apply(configure))
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

// This is a hack which prevents the Kotlin Plugin from getting mad because it misses a js sub target
private class LenientJsPreset(project: Project) : KotlinJsIrTargetPreset(project) {
    public override fun instantiateTarget(name: String): KotlinJsIrTarget {
        @Suppress("INVISIBLE_SETTER")
        return project.objects.newInstance(KotlinJsIrTarget::class.java, project, platformType, true).apply {
            isMpp = true
        }
    }
}
