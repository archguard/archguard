package com.thoughtworks.archgard.scanner.domain.project

import com.thoughtworks.archgard.scanner.infrastructure.Processor
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Paths

class ProjectOperator(val projectInfo: ProjectInfo) {
    private val log = LoggerFactory.getLogger(ProjectOperator::class.java)
    val compiledProjectMap = mutableMapOf<String, CompiledProject>()
    val workspace: File = createTempDir()
    val sql: String by lazy { projectInfo.sql }

    fun cloneAndBuildAllRepo() {
        log.info("workSpace is: ${workspace.toPath()}")
        this.projectInfo.getRepoList()
                .forEach(this::cloneAndBuildSingleRepo)
    }


    fun cloneAllRepo() {
        log.info("workSpace is: ${workspace.toPath()}")
        this.projectInfo.getRepoList()
                .forEach(this::cloneSingleRepo)
    }

    private fun cloneSingleRepo(repo: String) {
        val repoWorkSpace = createTempDir(directory = workspace)
        log.info("workSpace is ${repoWorkSpace.toPath()} repo is: $repo")
        getSource(repoWorkSpace, repo)
        compiledProjectMap[repo] = CompiledProject(repo, repoWorkSpace, getBuildTool(repoWorkSpace), this.projectInfo.sql)
    }

    private fun cloneAndBuildSingleRepo(repo: String) {
        val repoWorkSpace = createTempDir(directory = workspace)
        log.info("workSpace is ${repoWorkSpace.toPath()} repo is: $repo")
        getSource(repoWorkSpace, repo)
        val buildTool = getBuildTool(repoWorkSpace)
        buildSource(repoWorkSpace, buildTool)
        compiledProjectMap[repo] = CompiledProject(repo, repoWorkSpace, buildTool, this.projectInfo.sql)
    }

    private fun getSource(workspace: File, repo: String) {
        when (this.projectInfo.repoType) {
            "GIT" -> cloneByGit(workspace, repo)
            "SVN" -> cloneBySvn(workspace, repo)
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

    private fun cloneByGit(workspace: File, repo: String) {
        val cloneCmd = Git.cloneRepository()
                .setDirectory(workspace)
                .setURI(repo)

        if (projectInfo.hasAuthInfo()) {
            cloneCmd.setCredentialsProvider(UsernamePasswordCredentialsProvider(projectInfo.username,
                    projectInfo.getDeCryptPassword()))
        }

        cloneCmd.call()
    }

    private fun cloneBySvn(workspace: File, repo: String) {
        val cmdList = if (projectInfo.hasAuthInfo()) {
            listOf("svn", "checkout",
                    repo, Paths.get("./").normalize().toString(),
                    "--username", projectInfo.username,
                    "--password", projectInfo.getDeCryptPassword())
        } else {
            listOf("svn", "checkout",
                    repo, Paths.get("./").normalize().toString())
        }

        val pb = ProcessBuilder(cmdList)
        Processor.executeWithLogs(pb, workspace)
    }
}
