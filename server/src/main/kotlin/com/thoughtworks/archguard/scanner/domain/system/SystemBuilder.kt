package com.thoughtworks.archguard.scanner.domain.system

import com.thoughtworks.archguard.scanner.domain.exception.CloneSourceCodeException
import com.thoughtworks.archguard.scanner.domain.scanner.git.GitCommand
import com.thoughtworks.archguard.scanner.domain.system.BuildTool.NONE
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

    fun clone() {
        log.info("workspace is: ${workspace.toPath()}")
        this.systemInfo.getRepoList()
            .forEach { repo ->
                cloneSourceCode(repo)
                scannedProjects.add(
                    ScanProject(
                        repo,
                        workspace,
                        NONE,
                        systemInfo.sql,
                        systemInfo.language,
                        systemInfo.codePath,
                        systemInfo.branch
                    )
                )
            }
    }

    private fun cloneSourceCode(repo: String) {
        log.info("workspace is located at ${workspace.toPath()}, repo is located at: $repo")
        val exitCode = when (systemInfo.repoType) {
            "GIT" -> cloneByGitCli(workspace, repo)
            "SVN" -> cloneBySvn(workspace, repo)
            "LOCAL" -> cloneByCp(workspace, repo)
            else -> -1
        }
        if (exitCode != 0) {
            throw CloneSourceCodeException("Fail to clone source with exitCode $exitCode")
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

    private fun cloneByCp(workspace: File, repo: String): Int {
        val cmdList = listOf("cp", "-r", repo, workspace.path)
        val pb = ProcessBuilder(cmdList)
        return Processor.executeWithLogs(pb, workspace, logStream)
    }
}
