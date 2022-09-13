package com.thoughtworks.archguard.scanner.domain.system

import com.thoughtworks.archguard.scanner.domain.exception.CloneSourceException
import com.thoughtworks.archguard.scanner.domain.exception.CompileException
import com.thoughtworks.archguard.scanner.domain.scanner.git.GitCommand
import com.thoughtworks.archguard.scanner.infrastructure.command.Processor
import com.thoughtworks.archguard.scanner.infrastructure.command.StreamConsumer
import org.slf4j.LoggerFactory
import org.springframework.util.FileSystemUtils
import java.io.File
import java.net.URLEncoder
import java.nio.file.Paths

class SystemBuilder(
    val systemInfo: SystemInfo,
    val workspace: File,
    val logStream: StreamConsumer,
) {
    private val log = LoggerFactory.getLogger(SystemBuilder::class.java)
    val scannedProjects = mutableSetOf<ScanProject>()
    val sql: String by lazy { systemInfo.sql }

    fun cloneAndBuildAllRepo() {
        log.info("workSpace is: ${workspace.toPath()}")
        this.systemInfo.getRepoList()
            .forEach { repo ->
                if (systemInfo.repoType == "LOCAL") {
                    scannedProjects.add(
                        ScanProject(
                            repo,
                            File(repo),
                            BuildTool.NONE,
                            systemInfo.sql,
                            systemInfo.language,
                            repo,
                            systemInfo.branch
                        )
                    )
                } else {
                    // for archguard 1.0, it need to build to create jvm
                    if (systemInfo.isNecessaryBuild() && systemInfo.language.lowercase() == "jvm") {
                        cloneAndBuildSingleRepo(repo)
                    } else {
                        cloneSingleRepo(repo)
                    }
                }
            }
    }

    private fun cloneSingleRepo(repo: String) {
        log.info("workSpace is ${workspace.toPath()} repo is: $repo")
        val exitCode = getSource(workspace, repo)
        if (exitCode != 0) {
            throw CloneSourceException("Fail to clone source with exitCode $exitCode")
        }
        scannedProjects.add(
            ScanProject(
                repo,
                workspace,
                BuildTool.NONE,
                this.systemInfo.sql,
                systemInfo.language,
                systemInfo.codePath,
                systemInfo.branch
            )
        )
    }

    private fun cloneAndBuildSingleRepo(repo: String) {
        log.info("workSpace is ${workspace.toPath()} repo is: $repo")
        val exitCode = getSource(workspace, repo)
        if (exitCode != 0) {
            throw CloneSourceException("Fail to clone source with exitCode $exitCode")
        }

        val buildTool = getBuildTool(workspace)
        buildSource(workspace, buildTool)
        scannedProjects.add(
            ScanProject(
                repo,
                workspace,
                buildTool,
                this.systemInfo.sql,
                systemInfo.language,
                systemInfo.codePath,
                systemInfo.branch
            )
        )
    }

    private fun getSource(workspace: File, repo: String): Int {
        val wrongRepoType = -1

        when (this.systemInfo.repoType) {
            "GIT" -> return cloneByGitCli(workspace, repo)
            "SVN" -> return cloneBySvn(workspace, repo)
            "ZIP" -> return cloneByZip(workspace, repo)
        }

        return wrongRepoType
    }

    private fun buildSource(workspace: File, buildTool: BuildTool) {
        val pb: ProcessBuilder = when (buildTool) {
            BuildTool.MAVEN -> ProcessBuilder("mvn", "clean", "test", "-DskipTests")
            BuildTool.GRADLE -> ProcessBuilder("./gradlew", "clean", "testClasses")
            BuildTool.NONE -> throw CompileException("Fail to identify build tool for compile")
        }

        Processor.executeWithLogs(pb, workspace, logStream)
    }

    private fun getBuildTool(workspace: File): BuildTool {
        return when {
            workspace.listFiles().orEmpty().any { it.name == "pom.xml" } -> BuildTool.MAVEN
            workspace.listFiles().orEmpty().any { it.name.startsWith("build.gradle") } -> BuildTool.GRADLE
            else -> BuildTool.NONE
        }
    }

    private fun cloneByGitCli(workspace: File, repo: String): Int {
        // todo: display repo with password
        val repoCombineWithAuthInfo = processGitUrl(repo)

        // TODO: Can use [git clone XXX --shallow-since "2020-09-05T00:00:00"] to shallow clone with a history after the specified time.
        // So we can get the files often modified in git commit after the specified time via [coca git -t] and clone faster
        val gitCommand = GitCommand(workspace, systemInfo.branch, false, arrayListOf(), logStream)

        return if (isGitRepository(workspace)) {
            log.debug("Going to fetch repo: {}", workspace)
            gitCommand.pullCode()
        } else {
            if (workspace.exists()) {
                FileSystemUtils.deleteRecursively(workspace)
                workspace.mkdir()
            }

            log.debug("Going to clone {}", repoCombineWithAuthInfo)
            gitCommand.clone(repoCombineWithAuthInfo, 2048, systemInfo.branch)
        }
    }

    private fun isGitRepository(workingFolder: File): Boolean {
        return File(workingFolder, ".git").isDirectory
    }

    private fun processGitUrl(repo: String): String {
        return if (systemInfo.hasAuthInfo()) {
            repo.replace("//", "//${urlEncode(systemInfo.username)}:${urlEncode(systemInfo.getDeCryptPassword())}@")
        } else {
            repo
        }
    }

    private fun urlEncode(msg: String): String {
        return URLEncoder.encode(msg, "UTF-8")
    }

    private fun cloneBySvn(workspace: File, repo: String): Int {
        val cmdList = if (systemInfo.hasAuthInfo()) {
            listOf(
                "svn", "checkout",
                repo, Paths.get("./").normalize().toString(),
                "--username", systemInfo.username,
                "--password", systemInfo.getDeCryptPassword()
            )
        } else {
            listOf(
                "svn", "checkout",
                repo, Paths.get("./").normalize().toString()
            )
        }
        log.debug("command to be executed: {}", cmdList)

        val pb = ProcessBuilder(cmdList)
        return Processor.executeWithLogs(pb, workspace, logStream)
    }

    private fun cloneByZip(workspace: File, repo: String): Int {
        // unzip .zip file to workspace
        // unzip -d /temp test.zip
        val cmdList = listOf("unzip", repo)
        val pb = ProcessBuilder(cmdList)
        return Processor.executeWithLogs(pb, workspace, logStream)
    }
}
