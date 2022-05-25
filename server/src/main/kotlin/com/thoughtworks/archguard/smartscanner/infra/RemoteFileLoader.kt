package com.thoughtworks.archguard.smartscanner.infra

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import kotlin.io.path.exists
import kotlin.system.measureTimeMillis

object RemoteFileLoader {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private fun isInstalled(fileName: String): Boolean = Paths.get(fileName).exists()
    private fun install(fileName: String, downloadUrl: String) {
        val sourceUrl = URL(downloadUrl)
        val targetPath = Paths.get(fileName)

        logger.debug("downloading...")
        logger.debug("| $sourceUrl -> $targetPath |")

        val cost = measureTimeMillis {
            sourceUrl.openStream().use {
                Files.copy(it, targetPath, StandardCopyOption.REPLACE_EXISTING)
            }
        }
        logger.debug("downloading finished in $cost ms")
    }

    fun load(fileName: String, downloadUrl: String) {
        if (!isInstalled(fileName)) install(fileName, downloadUrl)
    }

    fun load(workspace: File, fileName: String, downloadUrl: String) {
        if (!isInstalled(fileName)) install(fileName, downloadUrl)
        copy2Workspace(workspace, fileName)
    }

    private fun copy2Workspace(workspace: File, fileName: String) {
        logger.info("copy installed tools into workspace")
        FileOperator.copyTo(File(fileName), File("$workspace/$fileName"))
        try {
            val chmod = ProcessBuilder("chmod", "+x", fileName)
            chmod.directory(workspace)
            chmod.start().waitFor()
        } catch (ex: Exception) {
            logger.warn("chmod +x $fileName tool Exception")
        }
    }
}
