package org.archguard.scanner.gradle.plugin

import org.archguard.scanner.core.AnalyserSpec
import org.archguard.scanner.ctl.command.ScannerCommand
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

        println("extension: ${extension.serverUrl}")
//        val command = toCommand(extension)

        project.tasks.register(TASK_NAME, ArchguardScanTask::class.java) {
//            it.command = command
            it.group = "verification"
            it.description = "Scan the project with Archguard"
        }
    }
}

private fun toCommand(extension: ArchguardExtension): ScannerCommand {
    return ScannerCommand(
        serverUrl = extension.serverUrl,
        language = extension.language,
        features = extension.features,
        path = ".",
        output = extension.output,
        type = org.archguard.scanner.core.context.AnalyserType.SOURCE_CODE,
        systemId = extension.systemId,
        slots = extension.slotContainer.map { slot ->
            AnalyserSpec(
                identifier = slot.identifier,
                host = slot.host,
                version = slot.version,
                jar = slot.jar,
                className = slot.className
            )
        }.toList()
    )
}
