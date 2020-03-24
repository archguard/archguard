package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.tbs.TestBadSmell
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate


interface TestBadSmellDao {

    @SqlBatch("insert into testBadSmell (id, line, description, file_name, type) " +
            "values (:testBadSmell.id, :testBadSmell.line, :testBadSmell.description, :testBadSmell.fileName,:testBadSmell.type)")
    fun saveAll(@BindBean("testBadSmell") testBadSmellList: List<TestBadSmell>)

    @SqlUpdate("delete from testBadSmell where 1=1 ")
    fun deleteAll()

}