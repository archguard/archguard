package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgery
import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class ShotgunSurgeryRepositoryImpl(val jdbi: Jdbi) : ShotgunSurgeryRepository {

    override fun getShotgunSurgeryCommitIds(systemId: Long, limit: Int): List<String> {
        val sql = "select sum(1) as sum , commit_id from cognitive_complexity where changed_cognitive_complexity > 0 and system_id=:system_id " +
                "group by commit_id;"
        val cognitiveComplexityList = jdbi.withHandle<List<CognitiveComplexityPO>, Exception> {
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .mapTo(CognitiveComplexityPO::class.java)
                    .list()
        }
        return cognitiveComplexityList.filter { it.sum > limit }.map { it.commitId }
    }

    override fun getShotgunSurgery(commitIds: List<String>, limit: Long, offset: Long): List<ShotgunSurgery> {
        val sql = "select l.id as commitId, l.short_msg as commitMessage, e.old_path as oldPath, e.new_path as newPath " +
                "from change_entry e, commit_log l " +
                "where l.id = e.commit_id and e.commit_id in (:commit_ids) " +
                "order by l.commit_time desc LIMIT :limit OFFSET :offset"
        val shotgunSurgeryPOList = jdbi.withHandle<List<ShotgunSurgeryPO>, Exception> {
            it.createQuery(sql)
                    .bind("commit_ids", commitIds.joinToString(","))
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ShotgunSurgeryPO::class.java)
                    .list()
        }
        return ShotgunSurgeryPO.from(shotgunSurgeryPOList.groupBy { it.commitId })
    }
}