package com.thoughtworks.archguard.git.analyzer

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
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
            val commitMap = RowMapper { rs, ctx ->
                val commitId = rs.getString("id")



                RevCommit(id = commitId,
                        commit_time = rs.getInt("commit_time"),
                        committer_name = rs.getString("committer_name"),
                        rep_id = rs.getLong("rep_id"),
                        entries = setOf(ChangeEntry("", 4, "ADD"))
                )
            }


            val queryCommit = "select id, commit_time, committer_name, rep_id from RevCommit"
            it.createQuery(queryCommit)
                    .map(commitMap).list()
        }
    }

}