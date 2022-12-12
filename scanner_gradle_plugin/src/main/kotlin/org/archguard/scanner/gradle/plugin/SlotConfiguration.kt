package org.archguard.scanner.gradle.plugin

import groovy.lang.Closure
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

/**
 * Container for the custom Archguard plugin configuration DSL.
 */
class SlotConfiguration(project: Project) {
    var slot = project.container(SlotAttribute::class.java) {
        SlotAttribute(project)
    }

    fun slot(config: SlotAttributeContainer.() -> Unit) {
        slot.configure(object : Closure<Unit>(this, this) {
            fun doCall() {
                @Suppress("UNCHECKED_CAST")
                (delegate as? SlotAttributeContainer)?.let {
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