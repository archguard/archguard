package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.report.application.GitFileService
import com.thoughtworks.archguard.report.domain.githotfile.GitHotFile
import com.thoughtworks.archguard.report.domain.githotfile.GitPathChangeCount
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/git")
class GitFileController(val gitFileService: GitFileService) {

    @GetMapping("/hot-files")
    fun getGitHotFilesBySystemId(@PathVariable("systemId") systemId: Long) : List<GitHotFileDTO> {
        return gitFileService.getGitHotFilesBySystemId(systemId).map { GitHotFileDTO(it) }
    }

    // todo: rename to restful
    @GetMapping("/changes")
    fun getAllChanges(@PathVariable("systemId") systemId: Long) : List<GitChangeCount> {
        return gitFileService.getGitFileChanges(systemId).map { GitChangeCount(it) }
    }

    // todo: rename to restful
    @GetMapping("/path-change-count")
    fun getChangeCountByPath(@PathVariable("systemId") systemId: Long) : List<GitPathCount> {
        return gitFileService.getPathChangeCount(systemId).map { GitPathCount(it) }
    }
}

class GitPathCount(private val gitHotFile: GitPathChangeCount) {
    val name: String
        get() = "root/" + gitHotFile.path

    val value: Int
        get() = gitHotFile.changes
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
