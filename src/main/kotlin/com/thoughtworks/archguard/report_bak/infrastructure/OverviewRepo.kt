package com.thoughtworks.archguard.report_bak.infrastructure

import com.thoughtworks.archguard.report_bak.domain.repository.OverviewRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class OverviewRepo(@Autowired val jdbi: Jdbi, @Autowired val classStatisticRepo: ClassStatisticRepo) : OverviewRepository {
    override fun getGitCommit(): List<GitCommitDBO> =
            jdbi.withHandle<List<GitCommitDBO>, Nothing> {
                it.registerRowMapper(ConstructorMapper.factory(GitCommitDBO::class.java))
                it
                        .createQuery("select id, cmttr_email from scm_commit_log where 1 = 1")
                        .mapTo(GitCommitDBO::class.java)
                        .list()
            }

    override fun getCodeLinesCount(): Int = classStatisticRepo.getCodeLinesCount()


}