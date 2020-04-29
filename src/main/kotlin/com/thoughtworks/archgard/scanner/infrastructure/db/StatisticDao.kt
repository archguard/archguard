package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.statistic.Statistic
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface StatisticDao {
    @SqlBatch("insert into Statistic (id, projectName, packageName, typeName, `lines`, fanin, fanout, updateAt, createAt) " +
            "values (:statistic.id, :statistic.projectName, :statistic.packageName, :statistic.typeName," +
            " :statistic.lines, :statistic.fanIn, :statistic.fanOut, NOW(), NOW())")
    fun saveAll(@BindBean("statistic") statistic: List<Statistic>)

    @SqlUpdate("delete from Statistic where 1=1 ")
    fun deleteAll()

}