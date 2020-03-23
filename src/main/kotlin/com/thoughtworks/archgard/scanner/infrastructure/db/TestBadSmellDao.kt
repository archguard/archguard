package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.tbs.TestBadSmell
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch


interface TestBadSmellDao {

    @SqlBatch("insert into testBadSmell (id, line, description, file_name, type) " +
            "values (:testBadSmell.id, :testBadSmell.line, :testBadSmell.description, :testBadSmell.fileName,:testBadSmell.type)")
    fun saveAll(@BindBean("testBadSmell") testBadSmellList: List<TestBadSmell>)

    @SqlBatch("delete from testBadSmell where 1=1 ")
    fun deleteAll()

}