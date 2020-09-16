package com.thoughtworks.archgard.scanner.controller

import com.thoughtworks.archgard.scanner.domain.scanner.git.GitHotFile
import com.thoughtworks.archgard.scanner.domain.scanner.git.GitHotFileService
import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/systems/{systemId}/git-hot-files")
class GitHotFileController(val gitHotFileService: GitHotFileService) {

    @GetMapping
    fun getGitHotFilesBySystemId(@PathVariable("systemId") systemId: Long) : List<GitHotFileDTO> {
        return gitHotFileService.getGitHotFilesBySystemId(systemId).map { GitHotFileDTO(it) }
    }
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
