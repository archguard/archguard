package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.module.ClassVO
import com.thoughtworks.archguard.report.domain.redundancy.RedundancyRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class RedundancyRepositoryImpl(val jdbi: Jdbi) : RedundancyRepository {
    private val table = "select sum(1) sum, m.clzname as name, m.module as module " +
            "from JMethod m, JClass c, method_access ma, class_access ca " +
            "where ca.class_id=c.id  and ca.is_interface = false and ca.is_abstract = false " +
            "and ma.method_id = m.id and m.name != '<clinit>' and m.name != 'toString' " +
            "and m.name != 'toString' and m.name != 'equals' and m.name != 'hashCode' and m.name != 'clone'" +
            "and m.clzname = c.name and c.module=m.module and c.is_thirdparty = false " +
            "and m.system_id=:system_id and c.system_id=:system_id and ma.system_id=:system_id " +
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

    override fun getOneFieldClassCount(systemId: Long, limit: Long, offset: Long): Long {
        return 24
    }

    override fun getOneFieldClass(systemId: Long, limit: Long, offset: Long): List<ClassVO> {
        return listOf(ClassVO("a", "b.c.e.d", "e.java"),
                ClassVO("a", "b.c.e.d", "e1.java"))
    }


}