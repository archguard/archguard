package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.dataclumps.ClassDataClump
import com.thoughtworks.archguard.report.domain.dataclumps.DataClumpsRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class DataClumpsRepositoryImpl(val jdbi: Jdbi) : DataClumpsRepository {

    override fun getLCOM4AboveThresholdCount(systemId: Long, threshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(c.id) from JClass c inner join class_metrics m on m.class_id = c.id " +
                    "where c.system_id =:system_id and m.lcom4 > :lcom4"
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("lcom4", threshold)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getLCOM4AboveThresholdList(systemId: Long, threshold: Int, limit: Long, offset: Long): List<ClassDataClump> {
        val sql = "select c.id, c.system_id, c.name, c.module, m.lcom4 from JClass c " +
                "inner join class_metrics m on m.class_id = c.id " +
                "where c.system_id =:system_id and m.lcom4 > :lcom4 " +
                "order by m.lcom4 desc LIMIT :limit OFFSET :offset"
        val classWithLCOM4List = jdbi.withHandle<List<ClassWithLCOM4PO>, Exception> {
            it.registerRowMapper(ConstructorMapper.factory(ClassWithLCOM4PO::class.java))
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("lcom4", threshold)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(ClassWithLCOM4PO::class.java)
                    .list()
        }
        return classWithLCOM4List.map { it.toClassDataClump() }
    }
}