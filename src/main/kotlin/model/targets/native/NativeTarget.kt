package dev.kord.gradle.model.targets.native

import dev.kord.gradle.model.targets.AbstractTargetItem
import org.jetbrains.kotlin.gradle.dsl.KotlinCommonCompilerOptions
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

public typealias NativeTargetItem = AbstractTargetItem<out KotlinNativeTarget, KotlinCommonCompilerOptions>
