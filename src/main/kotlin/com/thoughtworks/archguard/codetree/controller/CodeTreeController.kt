package com.thoughtworks.archguard.codetree.controller

import com.thoughtworks.archguard.codetree.CodeTree
import com.thoughtworks.archguard.codetree.InitCodeTreeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/systems/{systemId}/code-tree")
class CodeTreeController(val codeTreeService: InitCodeTreeService) {
    @GetMapping
    fun getCodeTree(@PathVariable("systemId") systemId: Long): ResponseEntity<CodeTree> {
        val codeTree = codeTreeService.initCodeTree(systemId)
        return ResponseEntity.ok(codeTree)
    }
}
