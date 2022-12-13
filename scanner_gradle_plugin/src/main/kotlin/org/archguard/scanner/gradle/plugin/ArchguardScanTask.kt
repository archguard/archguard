package org.archguard.scanner.gradle.plugin

import org.archguard.scanner.ctl.command.ScannerCommand
import org.archguard.scanner.ctl.loader.AnalyserDispatcher
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.options.Option

abstract class ArchguardScanTask : DefaultTask() {
    init {
        description = "The scanner for Archguard"
    }

    @get:Input
    @get:Option(option = "command", description = "The scanner command")
    @get:Optional
    abstract var command: ScannerCommand

    @TaskAction
    fun executeScan() {
        logger.lifecycle("Archguard scan task start")

        AnalyserDispatcher().dispatch(command)

        logger.lifecycle("Archguard scan task end")
    }
}
