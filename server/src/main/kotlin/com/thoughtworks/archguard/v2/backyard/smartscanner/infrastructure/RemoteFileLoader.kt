package com.thoughtworks.archguard.v2.backyard.smartscanner.infrastructure

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.command.InMemoryConsumer
import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.jetbrains.annotations.TestOnly
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
    private fun install(fileName: String, downloadUrl: String, consumer: StreamConsumer) {
        val sourceUrl = URL(downloadUrl)
        val targetPath = Paths.get(fileName)

        log(consumer, "downloading...")

        var isDownloaded = false

        log(consumer, "| $sourceUrl -> $targetPath |")
        val cost = measureTimeMillis {
            try {
                sourceUrl.openStream().use {
                    Files.copy(it, targetPath, StandardCopyOption.REPLACE_EXISTING)
                }

                isDownloaded = true
            } catch (e: Exception) {
                log(consumer, e.message.toString())
            }
        }

        if (isDownloaded) {
            log(consumer, "downloading finished in $cost ms")
        }
    }

    private fun log(streamLog: StreamConsumer, information: String) {
        logger.debug(information)
        streamLog.consumeLine(information)
    }

    fun load(fileName: String, downloadUrl: String, logger: StreamConsumer) {
        if (!isInstalled(fileName)) install(fileName, downloadUrl, logger)
    }

    @TestOnly
    fun load(workspace: File, fileName: String, downloadUrl: String) {
        if (!isInstalled(fileName)) install(fileName, downloadUrl, InMemoryConsumer())
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
