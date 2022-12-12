package org.archguard.scanner.gradle.plugin

import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

/**
 * Container for the custom Archguard plugin configuration DSL.
 */
class SlotConfiguration(val name: String, project: Project) {
    var slot = project.container(SlotAttribute::class.java) {
        SlotAttribute(project)
    }

    fun slot(config: SlotAttribute.() -> Unit) {
        slot.configure(object : Closure<Unit>(this, this) {
            fun doCall() {
                @Suppress("UNCHECKED_CAST")
                (delegate as? SlotAttribute)?.let {
                    config(it)
                }
            }
        })
    }

    fun slot(config: Closure<Unit>) {
        slot.configure(config)
    }
}

internal typealias SlotConfigContainer = NamedDomainObjectContainer<SlotConfiguration>

fun SlotConfigContainer.slot(action: SlotAttributeContainer.() -> Unit) {
//    @Suppress("UNCHECKED_CAST")
//    (action as? SlotAttributeContainer)?.let {
//        action(it)
//    }
    (this as? ExtensionAware)
        ?.extensions
        ?.getByName("slot")
        ?.let { it as?  SlotAttributeContainer }
        ?.apply(action)
}
