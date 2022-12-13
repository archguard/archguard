package org.archguard.scanner.gradle.plugin

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

const val EXTENSION_NAME = "archguard"
const val TASK_NAME = "scanArchguard"

abstract class ArchguardPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME,
            ArchguardExtension::class.java, project
        )

        project.tasks.register(TASK_NAME, ArchguardScanTask::class.java) {
//            it.tag.set(extension.tag)
//            it.outputFile.set(extension.outputFile)
        }
    }
}
