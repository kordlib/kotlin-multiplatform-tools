package dev.kord.gradle

import org.gradle.api.Project

internal fun <T> Project.configureExtension(name: String, configure: T.() -> Unit) {
    if(extensions.findByName(name) == null) {
        return logger.warn("Please apply multiplatform plugin after plugin supplying extension: $name")
    }

    extensions.configure<T>(name, configure)
}
