package com.thoughtworks.archguard.git.analyzer

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/git/")
class GitController(@Autowired val git: GitAnalyzer) {


    @GetMapping("scatterCommits")
    fun scatterCommits(): List<Commit> {
        return git.findScatterCommits()
    }
}