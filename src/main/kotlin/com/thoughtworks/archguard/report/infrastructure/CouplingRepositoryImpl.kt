package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.report.domain.coupling.CouplingRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class CouplingRepositoryImpl(val jdbi: Jdbi) : CouplingRepository {
    override fun getCoupling(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int, offset: Long, limit: Long, orderByFanIn: Boolean): List<ClassCoupling> {
        var orderSqlPiece = "order by fanIn desc, fanOut desc "
        if (!orderByFanIn) {
            orderSqlPiece = "order by fanOut desc, fanIn desc "
        }
        return jdbi.withHandle<List<ClassCoupling>, Exception> {
            val sql = "select jc.id as id, jc.module as moduleName, jc.name as classFullName, " +
                    "(cc.inner_fan_in + cc.outer_fan_in) as fanIn, (cc.inner_fan_out + cc.outer_fan_out) as fanOut " +
                    "from class_coupling cc " +
                    "JOIN JClass jc on cc.system_id = jc.system_id and cc.class_id = jc.id where cc.system_id=:systemId " +
                    "and ((cc.inner_fan_in + cc.outer_fan_in)> :classFanInThreshold or (cc.inner_fan_out + cc.outer_fan_out) > :classFanOutThreshold)" +
                    orderSqlPiece + ", moduleName, classFullName limit :limit offset :offset"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("offset", offset)
                    .bind("limit", limit)
                    .bind("classFanInThreshold", classFanInThreshold)
                    .bind("classFanOutThreshold", classFanOutThreshold)
                    .mapTo(ClassCouplingPO::class.java).list().map { it.toClassCoupling() }
        }
    }

    override fun getCouplingCount(systemId: Long, classFanInThreshold: Int, classFanOutThreshold: Int): Long {
        return jdbi.withHandle<Long, Exception> {
            val sql = "select count(1)" +
                    "from class_coupling cc " +
                    "JOIN JClass jc on cc.system_id = jc.system_id and cc.class_id = jc.id where cc.system_id=:systemId " +
                    "and ((cc.inner_fan_in + cc.outer_fan_in)> :classFanInThreshold or (cc.inner_fan_out + cc.outer_fan_out) > :classFanOutThreshold)"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .bind("classFanInThreshold", classFanInThreshold)
                    .bind("classFanOutThreshold", classFanOutThreshold)
                    .mapTo(Long::class.java).one()
        }
    }

    override fun getAllCoupling(systemId: Long): List<ClassCoupling> {
        return jdbi.withHandle<List<ClassCoupling>, Exception> {
            val sql = "select jc.id as id, jc.module as moduleName, jc.name as classFullName, " +
                    "(cc.inner_fan_in + cc.outer_fan_in) as fanIn, (cc.inner_fan_out + cc.outer_fan_out) as fanOut " +
                    "from class_coupling cc " +
                    "JOIN JClass jc on cc.system_id = jc.system_id and cc.class_id = jc.id where cc.system_id=:systemId " +
                    "order by fanIn desc, fanOut desc, moduleName, classFullName"
            it.createQuery(sql)
                    .bind("systemId", systemId)
                    .mapTo(ClassCouplingPO::class.java).list().map { it.toClassCoupling() }
        }
    }
}