package com.thoughtworks.archguard.code.controller

import com.thoughtworks.archguard.code.CodeTree
import com.thoughtworks.archguard.code.InitCodeTreeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/{projectId}/code-tree")
class CodeTreeController(val codeTreeService: InitCodeTreeService) {
    @GetMapping("/")
    fun getCodeTree(@PathVariable("projectId") projectId: Long): ResponseEntity<CodeTree> {
        val codeTree = codeTreeService.initCodeTree(projectId)
        return ResponseEntity.ok(codeTree)
    }
}
