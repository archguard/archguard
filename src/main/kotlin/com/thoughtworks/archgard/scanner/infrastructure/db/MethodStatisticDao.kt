package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.statistic.MethodStatistic
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface MethodStatisticDao {
    @SqlBatch("insert into MethodStatistic (id, systemId, moduleName, packageName, typeName, methodName, `lines`, updateAt, createAt) " +
            "values (:methodStatistic.id, :methodStatistic.systemId, :methodStatistic.moduleName, :methodStatistic.packageName, :methodStatistic.typeName, :methodStatistic.methodName," +
            " :methodStatistic.lines, NOW(), NOW())")
    fun saveAll(@BindBean("methodStatistic") methodStatistic: List<MethodStatistic>)

    @SqlUpdate("delete from MethodStatistic where 1=1 ")
    fun deleteAll()
}