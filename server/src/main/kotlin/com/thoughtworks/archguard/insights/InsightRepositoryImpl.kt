package com.thoughtworks.archguard.insights

import com.thoughtworks.archguard.insights.domain.ScaModelDto
import com.thoughtworks.archguard.system_info.domain.SystemInfo
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class InsightRepositoryImpl(val jdbi: Jdbi) : InsightRepository {
    override fun filterByCondition(id: Long): List<ScaModelDto> {
        val sql =
            "select dep_artifact, dep_group, dep_version, dep_name" +
                    " from project_composition_dependencies where system_id = :id "

        return jdbi.withHandle<List<ScaModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ScaModelDto::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(ScaModelDto::class.java)
                .list()
        }
    }

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
}
