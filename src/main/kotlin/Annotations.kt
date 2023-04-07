package dev.kord.gradle

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.FIELD, AnnotationTarget.PROPERTY)
@RequiresOptIn
internal annotation class Internal

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@DslMarker
internal annotation class Dsl
