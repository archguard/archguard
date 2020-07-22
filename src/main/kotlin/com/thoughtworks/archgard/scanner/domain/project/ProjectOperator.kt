package com.thoughtworks.archgard.scanner.domain.project

import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths

class ProjectOperator(val projectInfo: ProjectInfo) {
    private val log = LoggerFactory.getLogger(ProjectOperator::class.java)

    fun build(): CompiledProject {
        val workspace = createTempDir()
        log.info("workspace is: {}, repo is: {}", workspace.toPath().toString(), this.projectInfo.repo)
        getSource(workspace)
        val buildTool = getBuildTool(workspace)
        buildSource(workspace, buildTool)
        return CompiledProject(this.projectInfo.repo, workspace, buildTool, this.projectInfo.sql)
    }

    fun getSource(): CompiledProject {
        val workspace = createTempDir()
        log.info("workspace is: {}, repo is: {}", workspace.toPath().toString(), this.projectInfo.repo)
        getSource(workspace)
        return CompiledProject(this.projectInfo.repo, workspace, getBuildTool(workspace), this.projectInfo.sql)
    }

    private fun getSource(workspace: File) {
        when (this.projectInfo.repoType) {
            "GIT" -> cloneByGit(workspace, projectInfo)
            "SVN" -> cloneBySvn(workspace, projectInfo)
        }
    }

    private fun buildSource(workspace: File, buildTool: BuildTool) {
        val pb: ProcessBuilder = when (buildTool) {
            BuildTool.MAVEN -> ProcessBuilder("./mvnw", "clean", "compile")
            BuildTool.GRADLE -> ProcessBuilder("./gradlew", "clean", "classes")
        }
        Processor.executeWithLogs(pb, workspace)
    }

    private fun getBuildTool(workspace: File): BuildTool {
        if (workspace.listFiles().orEmpty().any { it.name == "pom.xml" }) {
            return BuildTool.MAVEN
        }
        return BuildTool.GRADLE
    }

    private fun cloneByGit(workspace: File, projectInfo: ProjectInfo) {
        val cloneCmd = Git.cloneRepository()
                .setDirectory(workspace)
                .setURI(this.projectInfo.repo)

        if (projectInfo.hasAuthInfo()) {
            cloneCmd.setCredentialsProvider(UsernamePasswordCredentialsProvider(projectInfo.username,
                    projectInfo.getDeCryptPassword()))
        }

        cloneCmd.call()
    }

    private fun cloneBySvn(workspace: File, projectInfo: ProjectInfo) {
        val cmdList = if (projectInfo.hasAuthInfo()) {
            listOf("svn", "checkout",
                    this.projectInfo.repo, Paths.get("./").normalize().toString(),
                    "--username", projectInfo.username,
                    "--password", projectInfo.password)
        } else {
            listOf("svn", "checkout",
                    this.projectInfo.repo, Paths.get("./").normalize().toString())
        }

        val pb = ProcessBuilder(cmdList)
        Processor.executeWithLogs(pb, workspace)
    }
}
