package com.thoughtworks.archguard.git.analyzer

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.spring4.JdbiFactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


interface GitAnalyzer {

    fun findScatterCommits(): List<Commit>
}

@Component
class GitAnalyzerByJdbi(@Autowired val jdbiFactoryBean: JdbiFactoryBean) : GitAnalyzer {
    private lateinit var jdbi: Jdbi

    @PostConstruct
    fun jdbi() {
        jdbi = jdbiFactoryBean.`object`
    }

    override fun findScatterCommits(): List<Commit> {
        return listOf<Commit>(Commit("1", 11111111, "adfasfgadfgsdfdsf", "a@b.com", 123123123, setOf(ChangeEntry("a", "b", 3, "ADD", "sdfsfsdff"))))
    }

}