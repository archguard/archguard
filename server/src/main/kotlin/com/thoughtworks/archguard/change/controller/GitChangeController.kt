package com.thoughtworks.archguard.change.controller

import com.thoughtworks.archguard.change.application.GitChangeService
import com.thoughtworks.archguard.change.domain.GitHotFile
import com.thoughtworks.archguard.change.domain.GitPathChangeCount
import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/{systemId}/change")
class GitChangeController(val gitChangeService: GitChangeService) {

    @GetMapping("/hot-files")
    fun getGitHotFilesBySystemId(@PathVariable("systemId") systemId: Long): List<GitHotFileDTO> {
        return gitChangeService.getGitHotFilesBySystemId(systemId).map { GitHotFileDTO(it) }
    }

    @GetMapping("/commit-ids")
    fun getChangesInRange(
        @PathVariable("systemId") systemId: Long,
        @RequestParam(value = "startTime", required = true) startTime: String,
        @RequestParam(value = "endTime", required = true) endTime: String,
    ): List<String> {
        return gitChangeService.getChangesByRange(systemId, startTime, endTime)
    }

    @GetMapping("/path-change-count")
    fun getChangeCountByPath(@PathVariable("systemId") systemId: Long): List<GitPathCount> {
        return gitChangeService.getPathChangeCount(systemId).map { GitPathCount(it) }
    }

    @GetMapping("/unstable-file")
    fun getHighFrequencyChangeAndLongLines(@PathVariable("systemId") systemId: Long): List<GitPathChangeCount> {
        return gitChangeService.getUnstableFile(systemId)
    }
}

class GitPathCount(private val gitHotFile: GitPathChangeCount) {
    val name: String
        get() = "root/" + gitHotFile.path

    val value: Int
        get() = gitHotFile.changes

    val lines: Int
        get() = gitHotFile.lineCount
}

class GitChangeCount(private val gitHotFile: GitHotFile) {
    val name: String
        get() = "root/" + gitHotFile.path

    val value: Int
        get() = gitHotFile.modifiedCount
}

class GitHotFileDTO(private val gitHotFile: GitHotFile) {
    val jclassId: String
        get() = gitHotFile.jclassId!!

    val systemId: Long
        get() = gitHotFile.systemId

    val moduleName: String
        get() {
            return if (gitHotFile.moduleName != null) return gitHotFile.moduleName else ""
        }

    val packageName: String
        get() = JClassVO(gitHotFile.className!!, moduleName).getPackageName()

    val typeName: String
        get() = JClassVO(gitHotFile.className!!, moduleName).getTypeName()

    val modifiedCount: Int
        get() = gitHotFile.modifiedCount
}
