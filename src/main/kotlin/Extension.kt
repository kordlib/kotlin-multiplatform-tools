package dev.kord.gradle

import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input

public interface KotlinMultiplatformProjectExtension {
    @get:Input
    public val configurePublishing: Property<Boolean>

    @get:Input
    public val snapshotRepository: Property<String>

    @get:Input
    public val releaseRepository: Property<String>

    @get:Input
    public val projectName: Property<String>

    @get:Input
    public val description: Property<String>
}
