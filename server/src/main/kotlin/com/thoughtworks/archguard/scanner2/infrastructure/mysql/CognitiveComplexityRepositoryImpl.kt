package com.thoughtworks.archguard.scanner2.infrastructure.mysql

import org.archguard.threshold.CognitiveComplexity
import com.thoughtworks.archguard.scanner2.domain.repository.CognitiveComplexityRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class CognitiveComplexityRepositoryImpl(val jdbi: Jdbi) : CognitiveComplexityRepository {
    override fun saveAll(systemId: Long, cognitiveComplexityList: List<CognitiveComplexity>) {
        jdbi.useHandle<Exception> {
            it.createUpdate("delete from metric_cognitive_complexity where system_id=:system_id")
                .bind("system_id", systemId)
                .execute()
        }
        cognitiveComplexityList.forEach {
            save(it)
        }
    }

    private fun save(cognitiveComplexity: CognitiveComplexity) {
        jdbi.useHandle<Exception> {
            it.createUpdate("insert into metric_cognitive_complexity (id, commit_id, changed_cognitive_complexity, path, system_id) values (:id, :commit_id, :changed_cognitive_complexity,:path, :system_id)")
                .bind("id", UUID.randomUUID().toString())
                .bind("system_id", cognitiveComplexity.systemId)
                .bind("commit_id", cognitiveComplexity.commitId)
                .bind("changed_cognitive_complexity", cognitiveComplexity.changedCognitiveComplexity)
                .bind("path", cognitiveComplexity.path)
                .execute()
        }
    }
}
