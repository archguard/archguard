package com.thoughtworks.archguard.scanner.domain.scanner.git

import org.slf4j.LoggerFactory
import java.io.File
import java.util.regex.Pattern

// align to GoCD for better git clone
// https://github.com/gocd/gocd/blob/master/domain/src/main/java/com/thoughtworks/go/domain/materials/git/GitCommand.java
class GitCommand {
    private val log = LoggerFactory.getLogger(GitCommand::class.java)

    private val GIT_SUBMODULE_STATUS_PATTERN: Pattern = Pattern.compile("^.[0-9a-fA-F]{40} (.+?)( \\(.+\\))?$")
    private val GIT_SUBMODULE_URL_PATTERN: Pattern = Pattern.compile("^submodule\\.(.+)\\.url (.+)$")
    private val GIT_DIFF_TREE_PATTERN: Pattern = Pattern.compile("^(.)\\s+(.+)$")

    private val workingDir: File? = null
    private val secrets: List<String>? = null
    private val branch: String? = null
    private val isSubmodule = false


}