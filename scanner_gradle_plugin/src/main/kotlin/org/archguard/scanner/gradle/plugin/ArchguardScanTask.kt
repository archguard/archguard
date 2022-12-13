package org.archguard.scanner.gradle.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.*

abstract class ArchguardScanTask : DefaultTask() {
    init {
        description = "The scanner for Archguard"
    }

    @TaskAction
    fun executeScan() {
        logger.lifecycle("Archguard scan task start")

        logger.lifecycle("Archguard scan task end")
    }
}
