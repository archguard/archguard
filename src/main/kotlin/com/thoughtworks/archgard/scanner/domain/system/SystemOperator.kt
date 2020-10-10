package com.thoughtworks.archgard.scanner.domain.system

import com.thoughtworks.archgard.scanner.domain.exception.CloneSourceException
import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URLEncoder
import java.nio.file.Paths

class SystemOperator(val systemInfo: SystemInfo, val id: Long) {
    private val log = LoggerFactory.getLogger(SystemOperator::class.java)
    val compiledProjectMap = mutableMapOf<String, CompiledProject>()
    val workspace: File = createTempDir()
    val sql: String by lazy { systemInfo.sql }

    fun cloneAndBuildAllRepo() {
        log.info("workSpace is: ${workspace.toPath()}")
        this.systemInfo.getRepoList()
                .forEach { repo ->
                    if (systemInfo.isNecessaryBuild()) {
                        cloneAndBuildSingleRepo(repo)
                    } else {
                        cloneSingleRepo(repo)
                    }
                }
    }

    fun cloneAllRepo() {
        log.info("workSpace is: ${workspace.toPath()}")
        this.systemInfo.getRepoList()
                .forEach(this::cloneSingleRepo)
    }

    private fun cloneSingleRepo(repo: String) {
        val repoWorkSpace = createTempDir(directory = workspace)
        log.info("workSpace is ${repoWorkSpace.toPath()} repo is: $repo")
        val exitCode = getSource(repoWorkSpace, repo)
        if (exitCode != 0) {
            throw CloneSourceException("Fail to clone source with exitCode $exitCode")
        }
        compiledProjectMap[repo] = CompiledProject(repo, repoWorkSpace, BuildTool.NONE, this.systemInfo.sql)
    }

    private fun cloneAndBuildSingleRepo(repo: String) {
        val repoWorkSpace = createTempDir(directory = workspace)
        log.info("workSpace is ${repoWorkSpace.toPath()} repo is: $repo")
        val exitCode = getSource(repoWorkSpace, repo)
        if (exitCode != 0) {
            throw CloneSourceException("Fail to clone source with exitCode $exitCode")
        }

        val buildTool = getBuildTool(repoWorkSpace)
        buildSource(repoWorkSpace, buildTool)
        compiledProjectMap[repo] = CompiledProject(repo, repoWorkSpace, buildTool, this.systemInfo.sql)
    }

    private fun getSource(workspace: File, repo: String): Int {
        val WRONG_REPO_TYPE = -1

        when (this.systemInfo.repoType) {
            "GIT" -> return cloneByGitCli(workspace, repo)
            "SVN" -> return cloneBySvn(workspace, repo)
            "ZIP" -> return cloneByZip(workspace, repo)
        }
        return WRONG_REPO_TYPE
    }

    private fun buildSource(workspace: File, buildTool: BuildTool) {
        val pb: ProcessBuilder = when (buildTool) {
            BuildTool.MAVEN -> ProcessBuilder("./mvnw", "clean", "test", "-DskipTests")
            BuildTool.GRADLE -> ProcessBuilder("./gradlew", "clean", "testClasses")
            BuildTool.NONE -> return
        }
        Processor.executeWithLogs(pb, workspace)
    }

    private fun getBuildTool(workspace: File): BuildTool {
        if (workspace.listFiles().orEmpty().any { it.name == "pom.xml" }) {
            return BuildTool.MAVEN
        }
        return BuildTool.GRADLE
    }

    private fun cloneByGitCli(workspace: File, repo: String): Int {
        val repoCombineWithAuthInfo = processGitUrl(repo)
        log.debug("Going to clone {}", repoCombineWithAuthInfo)

        //TODO: Can use [git clone XXX --shallow-since "2020-09-05T00:00:00"] to shallow clone with a history after the specified time. 
        // So we can get the files often modified in git commit after the specified time via [coca git -t] and clone faster 
        val cmdList = listOf("git", "clone", repoCombineWithAuthInfo, workspace.absolutePath)
        log.debug("command to be executed: {}", cmdList)

        val pb = ProcessBuilder(cmdList)
        return Processor.executeWithLogs(pb, workspace)
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
            listOf("svn", "checkout",
                    repo, Paths.get("./").normalize().toString(),
                    "--username", systemInfo.username,
                    "--password", systemInfo.getDeCryptPassword())
        } else {
            listOf("svn", "checkout",
                    repo, Paths.get("./").normalize().toString())
        }
        log.info("command to be executed: {}", cmdList)

        val pb = ProcessBuilder(cmdList)
        return Processor.executeWithLogs(pb, workspace)
    }

    private fun cloneByZip(workspace: File, repo: String): Int {
        // unzip .zip file to workspace
        // unzip -d /temp test.zip
        val cmdList = listOf("unzip", repo)
        val pb = ProcessBuilder(cmdList)
        return Processor.executeWithLogs(pb, workspace)
    }
}
