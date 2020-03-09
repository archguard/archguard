package com.thoughtworks.archguard.git.analyzer

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.spring4.JdbiFactoryBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct


interface GitAnalyzer {

    fun findScatterCommits(): List<RevCommit>
}

@Component
class GitAnalyzerByJdbi(@Autowired val jdbiFactoryBean: JdbiFactoryBean) : GitAnalyzer {
    private lateinit var jdbi: Jdbi

    @PostConstruct
    fun jdbi() {
        jdbi = jdbiFactoryBean.`object`
    }

    override fun findScatterCommits(): List<RevCommit> {
        return jdbi.withHandle<List<RevCommit>, Exception> {
            it.createQuery("select * from RevCommit")
                    .mapToBean(RevCommit::class.java)
                    .list()
        }
    }

}