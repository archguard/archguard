package com.thoughtworks.archguard.report.infrastructure.cohesion

import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgery
import com.thoughtworks.archguard.report.domain.cohesion.ShotgunSurgeryRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class ShotgunSurgeryRepositoryImpl(val jdbi: Jdbi) : ShotgunSurgeryRepository {

    override fun getShotgunSurgeryCommitIds(systemId: Long, limit: Int): List<String> {
        val sql = "select sum(1) as sum , commit_id from metric_cognitive_complexity where changed_cognitive_complexity > 0 and system_id=:system_id " +
                "group by commit_id having sum > :limit"
        val cognitiveComplexityList = jdbi.withHandle<List<CognitiveComplexityPO>, Exception> {
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("limit", limit)
                    .mapTo(CognitiveComplexityPO::class.java)
                    .list()
        }
        return cognitiveComplexityList.map { it.commitId }
    }

    override fun getShotgunSurgery(commitIds: List<String>, limit: Long, offset: Long): List<ShotgunSurgery> {
        val list = jdbi.withHandle<List<String>, Exception> {
            it.createQuery("select id from scm_commit_log  " +
                    "where id in (${commitIds.joinToString("','", "'", "'")}) " +
                    "order by commit_time desc LIMIT :limit OFFSET :offset")
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(String::class.java)
                    .list()
        }

        val sql = "select l.id as commitId, l.short_msg as commitMessage, e.old_path as oldPath, e.new_path as newPath " +
                "from scm_change_entry e, scm_commit_log l " +
                "where l.id = e.commit_id and e.commit_id in (${list.joinToString("','", "'", "'")}) "
        val shotgunSurgeryPOList = jdbi.withHandle<List<ShotgunSurgeryPO>, Exception> {
            it.createQuery(sql)
                    .mapTo(ShotgunSurgeryPO::class.java)
                    .list()
        }
        return ShotgunSurgeryPO.from(shotgunSurgeryPOList.groupBy { it.commitId })
    }
}