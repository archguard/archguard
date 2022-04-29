package com.thoughtworks.archguard.smartscanner.infra

import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.exists
import kotlin.system.measureTimeMillis

object RemoteFileLoader {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private fun isInstalled(workspace: File, fileName: String): Boolean = workspace.toPath().resolve(fileName).exists()
    private fun install(workspace: File, fileName: String, downloadUrl: String) {
        val sourceUrl = URL(downloadUrl)
        val targetPath = workspace.toPath().resolve(fileName)

        logger.debug("downloading...")
        logger.debug("| $sourceUrl -> $targetPath |")

        val cost = measureTimeMillis {
            sourceUrl.openStream().use {
                Files.copy(it, targetPath, StandardCopyOption.REPLACE_EXISTING)
            }
        }
        logger.debug("downloading finished in $cost ms")
    }

    fun load(workspace: File, fileName: String, downloadUrl: String) {
        if (!isInstalled(workspace, fileName)) install(workspace, fileName, downloadUrl)
    }
}
