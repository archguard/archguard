package com.thoughtworks.archguard.report.infrastructure.redundancy

import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationPair
import com.thoughtworks.archguard.report.domain.redundancy.OverGeneralizationRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class OverGeneralizationRepositoryImpl(val jdbi: Jdbi) : OverGeneralizationRepository {
    val table = "select sum(1) sum, b, c.name , c.module " +
            "from code_class_parent cp, JClass c " +
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

    override fun getOverGeneralizationParentClassId(systemId: Long): List<String> {
        return jdbi.withHandle<List<String>, Exception> {
            it.createQuery("select b from ($table) a")
                    .bind("system_id", systemId)
                    .mapTo(String::class.java)
                    .list()
        }
    }

    override fun getOverGeneralizationPairList(parentClassIds: List<String>, limit: Long, offset: Long): List<OverGeneralizationPair> {
        return jdbi.withHandle<List<OverGeneralizationPair>, Exception> {
            val sql = "select c1.module as parentModuleName, c1.name as parentClzName, " +
                    "c2.module as childModuleName, c2.name as childClzName from " +
                    "(select a, b from code_class_parent where b in (<parentClassIds>)) as p " +
                    "inner join JClass c1 on p.b = c1.id " +
                    "inner join JClass c2 on p.a = c2.id " +
                    "limit :limit offset :offset"
            it.createQuery(sql)
                    .bindList("parentClassIds", parentClassIds)
                    .bind("offset", offset)
                    .bind("limit", limit)
                    .mapTo(OverGeneralizationPairPO::class.java).list().map { it.toOverGeneralizationPair() }
        }
    }

}