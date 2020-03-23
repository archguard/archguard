package com.thoughtworks.archgard.scanner.domain.hubexecutor

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.infrastructure.FileOperator
import com.thoughtworks.archgard.scanner.infrastructure.Processor.executeWithLogs
import org.eclipse.jgit.api.Git

class HubExecutor(private val context: ScanContext, private val manager: ScannerManager) : HubLifecycle {

    override fun execute() {
        getSource()
        buildSource()
        manager.execute(context)
    }

    override fun clean() {
        FileOperator.deleteDirectory(context.workspace)
    }

    private fun getSource() {
        Git.cloneRepository()
                .setDirectory(context.workspace)
                .setURI(context.repo)
                .call()
    }

    private fun buildSource() {
        val pb = if (context.workspace.listFiles().orEmpty().any { it.name == "pom.xml" }) {
            ProcessBuilder("./mvnw", "clean", "package", "-DskipTests")
        } else {
            ProcessBuilder("./gradlew", "clean", "build", "-x", "test")
        }
        executeWithLogs(pb, context.workspace)
    }

}