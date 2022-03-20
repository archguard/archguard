package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.javaext.tbs.TestBadSmell
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate


interface TestBadSmellDao {

    @SqlBatch("insert into testBadSmell (id, system_id, line, description, file_name, type) " +
            "values (:testBadSmell.id, :testBadSmell.systemId, :testBadSmell.line, :testBadSmell.description, :testBadSmell.fileName,:testBadSmell.type)")
    fun saveAll(@BindBean("testBadSmell") testBadSmellList: List<TestBadSmell>)

    @SqlUpdate("delete from testBadSmell where 1=1 ")
    fun deleteAll()

}