package org.archguard.scanner.gradle.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

const val EXTENSION_NAME = "archguard"
const val TASK_NAME = "scanArchguard"

abstract class ArchguardPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val slotContainer = project.container(SlotConfiguration::class.java, SlotConfigurationFactory(project))

        val extension = project.extensions.create(
            EXTENSION_NAME,
            ArchguardExtension::class.java,
            project,
            slotContainer
        )

        project.tasks.register(TASK_NAME, ArchguardScanTask::class.java) {
            it.group = "verification"
            it.description = "Scan the project with Archguard"

            //            it.tag.set(extension.tag)
//            it.outputFile.set(extension.outputFile)
        }
    }
}
