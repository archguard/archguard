package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.module.ClassVO
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class RedundancyRepositoryImpl(val jdbi: Jdbi) : RedundancyRepository {
    private val table = "select sum(1) sum,  m.clzname as name, m.module as module from JMethod m, JClass c " +
            "where CONVERT(m.access, SIGNED INTEGER)<1000 and m.name != '<clinit>' " +
            "and  m.clzname = c.name " +
            "and c.module=m.module " +
            "and c.is_thirdparty = false " +
            "and m.system_id=:system_id and c.system_id=:system_id " +
            "group by m.clzname, m.module having sum = 1"

    override fun getOneMethodClassCount(systemId: Long, limit: Long, offset: Long): Long {
        return jdbi.withHandle<Long, Exception> {
            it.createQuery("select count(1) from ($table) a")
                    .bind("system_id", systemId)
                    .mapTo<Long>(Long::class.java).one()
        }
    }

    override fun getOneMethodClass(systemId: Long, limit: Long, offset: Long): List<ClassVO> {
        return jdbi.withHandle<List<ClassVO>, Exception> {
            it.createQuery("$table order by clzname LIMIT :limit OFFSET :offset ")
                    .bind("system_id", systemId)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo<ClassPO>(ClassPO::class.java).list()
                    .map { p -> ClassVO.create(p.name, p.module) }
        }
    }
}