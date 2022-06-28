package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.CustomInsight
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class InsightRepositoryImpl(val jdbi: Jdbi) : InsightRepository {
    override fun saveInsight(insight: CustomInsight): Long {
        return jdbi.withHandle<Long, Nothing> {
            it.createUpdate(
                "insert into insight_custom" +
                        "(id, system_id, name, expression, schedule) " +
                        "values (:id, :systemId, :name, :expression, :schedule)"
            )
                .bindBean(insight)
                .executeAndReturnGeneratedKeys("id")
                .mapTo(Long::class.java)
                .one()
        }
    }

    override fun getInsightByName(name: String): CustomInsight? {
        return jdbi.withHandle<CustomInsight, Nothing> {
            it.createQuery(
                "select id, system_id as systemId, name, expression, schedule " +
                        "from insight_custom where name = :name"
            )
                .bind("name", name)
                .mapTo(CustomInsight::class.java)
                .firstOrNull()
        }
    }

    override fun deleteInsightByName(name: String): Int {
        return jdbi.withHandle<Int, Nothing> {
            it.createUpdate("delete from insight_custom where name = :name")
                .bind("name", name)
                .execute()
        }
    }
}
