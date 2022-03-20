package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs.BadSmell
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch


interface BadSmellDao {

    @SqlBatch("insert into metric_code_bad_smell (id, system_id, entity_name, line, description, size, type) " +
            "values (:badSmell.id, :badSmell.systemId, :badSmell.entityName, :badSmell.line, :badSmell.description, :badSmell.size,:badSmell.type)")
    fun saveAll(@BindBean("badSmell") badSmellList: List<BadSmell>)

}