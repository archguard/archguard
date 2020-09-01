package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.style.Style
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch
import org.jdbi.v3.sqlobject.statement.SqlUpdate

interface StyleDao {
    @SqlBatch("insert into CheckStyle (id, system_id, file, source, message, line, `column`, severity) " +
            "values (:checkStyle.id, :checkStyle.systemId, :checkStyle.file, :checkStyle.source, :checkStyle.message, :checkStyle.line, :checkStyle.column, :checkStyle.severity)")
    fun saveAll(@BindBean("checkStyle") styleList: List<Style>)

    @SqlUpdate("delete from CheckStyle where 1=1 ")
    fun deleteAll()

}