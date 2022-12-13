package org.archguard.scanner.gradle.plugin

import org.gradle.api.Named
import org.gradle.api.NamedDomainObjectFactory
import org.gradle.api.Project
import java.io.Serializable
import javax.inject.Inject

/**
 * Container for the custom Archguard plugin configuration DSL.
 */
abstract class SlotConfiguration : Named, Serializable {
    private val serialVersionUID = 1L

    private val mName: String
    private var project: Project

    abstract var identifier: String
    abstract var host: String
    abstract var version: String
    abstract var jar: String
    abstract var className: String
    abstract var slotType: String

    @Inject
    constructor(name: String, project: Project) {
        this.mName = name
        this.project = project
    }

    override fun getName(): String {
        return this.mName
    }
}

internal class SlotConfigurationFactory(private val project: Project) : NamedDomainObjectFactory<SlotConfiguration> {
    override fun create(name: String): SlotConfiguration {
        return project.objects.newInstance(SlotConfiguration::class.java, name, project)
    }
}
