package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.redundancy.DataClass
import com.thoughtworks.archguard.report.domain.redundancy.DataClassRepository
import com.thoughtworks.archguard.report.domain.redundancy.FieldVO
import com.thoughtworks.archguard.report.util.NameUtil
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class DataClassRepositoryImpl(val jdbi: Jdbi) : DataClassRepository {
    override fun getAllDataClassCount(systemId: Long): Long {
        val sql = "select count(distinct class_id) from data_class where system_id = :system_id"
        return jdbi.withHandle<Long, Exception> {
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getAllDataClassWithOnlyOneFieldCount(systemId: Long): Long {
        val sql = "select count(1) from (select class_id from data_class where system_id = :system_id group by class_id having count(field_id) = 1 ) as dc"
        return jdbi.withHandle<Long, Exception> {
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .mapTo(Long::class.java)
                    .one()
        }
    }

    override fun getAllDataClass(systemId: Long, limit: Long, offset: Long): List<DataClass> {
        val sql = "select distinct class_id as classId, module, name as className from data_class join JClass JC on data_class.class_id = JC.id and JC.system_id=:system_id and data_class.system_id=:system_id order by module, name limit :limit offset :offset"
        val dataClassPOs = jdbi.withHandle<List<DataClassPO>, Exception> {
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(DataClassPO::class.java)
                    .list()
        }
        val fieldSql = "select name, type from data_class join JField JF on data_class.field_id = JF.id and data_class.system_id = :system_id and JF.system_id = :system_id and class_id=:class_id"

        return dataClassPOs.map {
            val fieldNames = jdbi.withHandle<List<FieldPO>, Exception> { handle ->
                handle.createQuery(fieldSql)
                        .bind("system_id", systemId)
                        .bind("class_id", it.classId)
                        .mapTo(FieldPO::class.java)
                        .list()
            }
            DataClass(it.module, NameUtil.getPackageName(it.className), NameUtil.getClassName(it.className), fieldNames.map { FieldVO(it.name, it.type) })
        }

    }

    override fun getAllDataClassWithOnlyOneField(systemId: Long, limit: Long, offset: Long): List<DataClass> {
        val sql = "select distinct class_id as classId, module, name as className from data_class join JClass JC on data_class.class_id = JC.id and JC.system_id=:system_id and data_class.system_id=:system_id group by class_id, module, name having count(1)=1 order by module, name limit :limit offset :offset"
        val dataClassPOs = jdbi.withHandle<List<DataClassPO>, Exception> {
            it.createQuery(sql)
                    .bind("system_id", systemId)
                    .bind("limit", limit)
                    .bind("offset", offset)
                    .mapTo(DataClassPO::class.java)
                    .list()
        }
        val fieldSql = "select name, type from data_class join JField JF on data_class.field_id = JF.id and data_class.system_id = :system_id and JF.system_id = :system_id and class_id=:class_id"

        return dataClassPOs.map {
            val fieldNames = jdbi.withHandle<List<FieldPO>, Exception> { handle ->
                handle.createQuery(fieldSql)
                        .bind("system_id", systemId)
                        .bind("class_id", it.classId)
                        .mapTo(FieldPO::class.java)
                        .list()
            }
            DataClass(it.module, NameUtil.getPackageName(it.className), NameUtil.getClassName(it.className), fieldNames.map { FieldVO(it.name, it.type) })
        }
    }

}

data class DataClassPO(val classId: String, val module: String, val className: String)
data class FieldPO(val name: String, val type: String)