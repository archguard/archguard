package com.thoughtworks.archguard.scanner.domain.scanner.git

import com.thoughtworks.archguard.scanner.infrastructure.command.CommandLine
import com.thoughtworks.archguard.scanner.infrastructure.command.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.lang.String.format

// align to GoCD for better git clone
// https://github.com/gocd/gocd/blob/master/domain/src/main/java/com/thoughtworks/go/domain/materials/git/GitCommand.java
class GitCommand(
    val workingDir: File,
    val branch: String,
    val isSubmodule: Boolean,
    val secrets: List<String> = listOf()
) {
    private val log = LoggerFactory.getLogger(GitCommand::class.java)

    private fun cloneCommand(): CommandLine {
        return git().withArg("clone")
    }

    fun clone(url: String, depth: Int): Int {
        val gitClone = cloneCommand()
            .`when`(depth < Int.MAX_VALUE) { git -> git.withArg(String.format("--depth=%s", depth)) }
            .withArg(url).withArg(workingDir.absolutePath)

        return run(gitClone)
    }

    // todo: collection logs for frontend
    private fun run(cmd: CommandLine): Int {
        val processBuilder = ProcessBuilder(cmd.getCommandLine())
        return Processor.executeWithLogs(processBuilder, workingDir)
    }

    private fun git(): CommandLine {
        val git: CommandLine = CommandLine.createCommandLine("git").withEncoding("UTF-8")
        return git.withNonArgSecrets(secrets)
    }

    private fun gitWd(): CommandLine {
        return git().withWorkingDir(workingDir)
    }

    fun fetch(): Int {
        val gitFetch: CommandLine = gitWd().withArgs("fetch", "origin", "--prune", "--recurse-submodules=no")
        val result: Int = run(gitFetch)
        if (result != 0) {
            throw RuntimeException(format("git fetch failed for [%s]", workingDir))
        }

        return result
    }

    private fun git_C(): CommandLine {
        return git().withArgs("-C", workingDir.absolutePath)
    }
}