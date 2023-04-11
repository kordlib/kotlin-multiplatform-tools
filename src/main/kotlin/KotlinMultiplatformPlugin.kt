package dev.kord.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.gradle.plugins.signing.SigningExtension
import java.util.*

private const val extensionName: String = "kotlinMultiplatformProject"

public class KotlinMultiplatformPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        val extension =
            target.extensions.create(extensionName, KotlinMultiplatformProjectExtension::class.java)

        with(target) {
            afterEvaluate {
                if (extension.configurePublishing.convention(true).orNull == true) {
                    applyPublishing(extension)
                }
            }
        }
    }
}

private fun Project.applyPublishing(extension: KotlinMultiplatformProjectExtension) {
    configureExtension<PublishingExtension>("publishing") {
        publications {
            withType<MavenPublication> {
                if (tasks.findByName("dokkaHtml") != null) {
                    val platform = name.substringAfterLast('-')
                    val dokkaJar = tasks.register<Jar>("${platform}DokkaJar") {
                        archiveClassifier.set("javadoc")
                        destinationDirectory.set(buildDir.resolve(platform))
                        from(tasks.getByName("dokkaJavadoc"))
                    }
                    artifact(dokkaJar)
                } else {
                    logger.warn("Cannot configure publishing for $path because Dokka is not applied")
                }

                applyKordData(extension)
            }

            repositories {
                maven {
                    url = uri(
                        if (project.version.toString().endsWith("-SNAPSHOT")) {
                            extension.snapshotRepository.get()
                        } else {
                            extension.releaseRepository.get()
                        }
                    )

                    credentials {
                        username = System.getenv("NEXUS_USER")
                        password = System.getenv("NEXUS_PASSWORD")
                    }
                }
            }

        }
        applySigning(this)
    }
}

private fun Project.applySigning(publishingExtension: PublishingExtension) {
    configureExtension<SigningExtension>("signing") {
        val signingKey = findProperty("signingKey")?.toString()
        val signingPassword = findProperty("signingPassword")?.toString()
        if (signingKey != null && signingPassword != null) {
            useInMemoryPgpKeys(String(Base64.getDecoder().decode(signingKey)), signingPassword)
            publishingExtension.publications.withType<MavenPublication> {
                sign(this)
            }
        }
    }
}

context(Project)
private fun MavenPublication.applyKordData(extension: KotlinMultiplatformProjectExtension) {
    val projectName = extension.projectName.get()
    groupId = group.toString()
    artifactId = "$projectName-${artifactId}"
    version = version.toString()

    pom {
        name.set(extension.projectName.get())
        description.set(extension.description.get())
        url.set("https://github.com/kordlib/$projectName")

        organization {
            name.set("Kord")
            url.set("https://github.com/kordlib")
        }

        developers {
            developer {
                name.set("The Kord Team")
            }
        }

        issueManagement {
            system.set("GitHub")
            url.set("https://github.com/kordlib/$projectName/issues")
        }

        licenses {
            license {
                name.set("MIT")
                url.set("https://opensource.org/licenses/MIT")
            }
        }

        scm {
            connection.set("scm:git:ssh://github.com/kordlib/$projectName.git")
            developerConnection.set("scm:git:ssh://git@github.com:kordlib/$projectName.git")
            url.set("https://github.com/kordlib/$projectName")
        }
    }
}
