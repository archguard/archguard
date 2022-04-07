package com.thoughtworks.archguard.scanner.domain.scanner.git

import org.slf4j.LoggerFactory
import java.io.File
import java.util.regex.Pattern

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

    fun clone(url: String, depth: Integer) {
        val gitClone = cloneCommand()
            .`when`(depth < Int.MAX_VALUE) { git -> git.withArg(String.format("--depth=%s", depth)) }
            .withArg(url).withArg(workingDir.absolutePath)

        gitClone.run()
    }

    private fun git(): CommandLine {
        val git: CommandLine = CommandLine.createCommandLine("git").withEncoding("UTF-8")
        return git.withNonArgSecrets(secrets)
    }

    private fun git_C(): CommandLine {
        return git().withArgs("-C", workingDir.absolutePath)
    }
}