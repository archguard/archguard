package com.thoughtworks.archguard.insights.application.structure

import com.thoughtworks.archguard.insights.application.InsightModelDto
import com.thoughtworks.archguard.insights.application.StructureModelDto
import org.archguard.domain.insight.Query
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class StructureRepositoryImpl(val jdbi: Jdbi) : StructureRepository {
    override fun filterByCondition(query: Query): List<StructureModelDto> {
        var sql = "SELECT name, package_name FROM code_class "

        sql += query.toSQL()

        return jdbi.withHandle<List<StructureModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .mapTo(StructureModelDto::class.java)
                .list()
        }
    }

    override fun filterByConditionWithSystemId(id: Long, query: Query): List<StructureModelDto> {
        var sql = "SELECT name, package_name FROM code_class where system_id = :id"

        sql += query.toSQL("AND")

        return jdbi.withHandle<List<StructureModelDto>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(InsightModelDto::class.java))
            it.createQuery(sql)
                .bind("id", id)
                .mapTo(StructureModelDto::class.java)
                .list()
        }
    }
}