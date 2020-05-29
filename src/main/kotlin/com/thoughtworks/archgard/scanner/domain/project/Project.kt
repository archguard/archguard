package com.thoughtworks.archgard.scanner.domain.project

import com.thoughtworks.archgard.scanner.domain.project.BuildTool.GRADLE
import com.thoughtworks.archgard.scanner.domain.project.BuildTool.MAVEN
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.eclipse.jgit.api.Git
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths

class Project(val id: String, val projectName: String, val repo: String, val sql: String?, val repoType: String) {
    private val log = LoggerFactory.getLogger(Project::class.java)

    fun build(): CompiledProject {
        val workspace = createTempDir()
        log.info("workspace is: {}, repo is: {}", workspace.toPath().toString(), repo)
        getSource(workspace, this.repo, this.repoType)
        val buildTool = getBuildTool(workspace)
        buildSource(workspace, buildTool)
        return CompiledProject(repo, workspace, buildTool, sql)
    }

    fun getSource(): CompiledProject {
        val workspace = createTempDir()
        log.info("workspace is: {}, repo is: {}", workspace.toPath().toString(), repo)
        getSource(workspace, this.repo, this.repoType)
        return CompiledProject(repo, workspace, getBuildTool(workspace), sql)
    }

    private fun getSource(workspace: File, repo: String, repoType: String) {
        if ("GIT" == repoType) {
            Git.cloneRepository()
                    .setDirectory(workspace)
                    .setURI(repo)
                    .call()
        } else if ("SVN" == repoType) {
            val pb = ProcessBuilder(listOf("svn", "checkout", repo, Paths.get("./").normalize().toString()))
            Processor.executeWithLogs(pb, workspace)
        }
    }

    private fun buildSource(workspace: File, buildTool: BuildTool) {
        val pb: ProcessBuilder = when (buildTool) {
            MAVEN -> ProcessBuilder("./mvnw", "clean", "package", "-Dmaven.test.skip=true")
            GRADLE -> ProcessBuilder("./gradlew", "--continue", "clean", "build", "-x", "test")
        }
        Processor.executeWithLogs(pb, workspace)
    }

    private fun getBuildTool(workspace: File): BuildTool {
        if (workspace.listFiles().orEmpty().any { it.name == "pom.xml" }) {
            return MAVEN
        }
        return GRADLE
    }
}
