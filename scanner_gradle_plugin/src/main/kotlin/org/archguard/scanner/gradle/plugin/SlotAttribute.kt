package org.archguard.scanner.gradle.plugin

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

class SlotAttribute(project: Project) {
    var sourcePath: String = ""

    var attributes: MutableMap<String, String> = mutableMapOf()

    /**
     * Adds an attribute to the queue configuration.
     *
     * @param [attribute] the name of the attribute to set
     * @param [value] the value to set the attribute to
     */
    fun attribute(attribute: String, value: String) {
        @Suppress("UNCHECKED_CAST")
        val map = attributes as? MutableMap<String, String>
        map?.put(attribute, value)
    }
}

internal typealias SlotAttributeContainer = NamedDomainObjectContainer<SlotAttribute>
