package dev.kord.gradle.model

import dev.kord.gradle.*
import org.gradle.api.Project
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.language.base.plugins.LifecycleBasePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

public class ItemGroup(override val parent: HasSourceSets, override val name: String) : AbstractSubItem(), ItemParent {
    private val subItems = mutableListOf<SubItem>()

    override fun addItem(subItem: SubItem) {
        subItems.add(subItem)
    }

    context(Project)
    @Internal
    override fun KotlinMultiplatformExtension.apply() {
        configureSourceSets(this@ItemGroup)
        configureTasks(this@ItemGroup)
    }

    context(Project)
    private fun KotlinMultiplatformExtension.configureTasks(itemGroup: ItemGroup) {
        with(tasks) {
            fun mapToName(nameSelector: (HasSourceSets) -> String) = itemGroup.subItems
                .asSequence()
                .filterIsInstance<HasSourceSets>()
                .map(nameSelector)
                .toList()

            register(testTask) {
                group = LifecycleBasePlugin.VERIFICATION_GROUP
                dependsOn(mapToName(HasSourceSets::testTask))
            }
            register(compileKotlinTask) {
                group = LifecycleBasePlugin.BUILD_GROUP
                dependsOn(mapToName(HasSourceSets::compileKotlinTask))
            }
            register(compileTestKotlinTask) {
                group = LifecycleBasePlugin.BUILD_GROUP
                dependsOn(mapToName(HasSourceSets::compileTestKotlinTask))
            }
            register(publishToMavenLocalTask) {
                group = PublishingPlugin.PUBLISH_TASK_GROUP
                dependsOn(mapToName(HasSourceSets::publishToMavenLocalTask))
            }
            register(publishToMavenRepositoryTask) {
                group = PublishingPlugin.PUBLISH_TASK_GROUP
                dependsOn(mapToName(HasSourceSets::publishToMavenRepositoryTask))
            }
        }
    }

    context(Project)
    private fun KotlinMultiplatformExtension.configureSourceSets(itemGroup: ItemGroup) {
        with(sourceSets) {
            val mainSourceSet = create(itemGroup.mainSourceSet) {
                dependsOn(getByName(parent.mainSourceSet))
            }
            val testSourceSet = create(itemGroup.testSourceSet) {
                dependsOn(getByName(parent.testSourceSet))
            }
            applyDependencyHandlers()

            subItems.forEach { with(it) { apply() } }

            itemGroup.subItems.forEach {
                if (it is HasSourceSets) {
                    getByName(it.mainSourceSet) {
                        dependsOn(mainSourceSet)
                    }
                    getByName(it.testSourceSet) {
                        dependsOn(testSourceSet)
                    }
                }
            }
        }
    }
}

public inline fun ItemParent.group(name: String, configure: ItemGroup.() -> Unit) {
    contract {
        callsInPlace(configure, InvocationKind.EXACTLY_ONCE)
    }

    addItem(ItemGroup(if (this is ItemGroup) this else parent, name).apply(configure))
}
