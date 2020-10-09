package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.module.ClassVO
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class OverGeneralizationRepositoryImpl(val jdbi: Jdbi) : OverGeneralizationRepository {
    val table = "select sum(1) sum, b, c.name , c.module " +
            "from _ClassParent cp, JClass c " +
            "where cp.system_id=:system_id and c.system_id=:system_id " +
            "and c.id = b and is_thirdparty=false " +
            "group by b having sum = 1"

    override fun getOverGeneralizationCount(systemId: Long): Long {
        return jdbi.withHandle<Long, Exception> {
            it.createQuery("select count(1) from ($table) a")
                    .bind("system_id", systemId)
                    .mapTo<Long>(Long::class.java).one()
        }
    }

    override fun getOverGeneralizationList(systemId: Long, limit: Long, offset: Long): List<ClassVO> {
        return jdbi.withHandle<List<ClassVO>, Exception> {
            it.createQuery("$table order by name LIMIT :limit OFFSET :offset ")
                    .bind("system_id", systemId)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo<ClassPO>(ClassPO::class.java).list()
                    .map { p -> ClassVO.create(p.name, p.module) }
        }
    }
}