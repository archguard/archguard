package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.checkstyle.CheckStyle
import org.jdbi.v3.sqlobject.customizer.BindBean
import org.jdbi.v3.sqlobject.statement.SqlBatch

interface CheckStyleDao {
    @SqlBatch("insert into CheckStyle (id, file, source, message, line, `column`, severity) " +
            "values (:checkStyle.id, :checkStyle.file, :checkStyle.source, :checkStyle.message, :checkStyle.line, :checkStyle.column, :checkStyle.severity)")
    fun saveAll(@BindBean("checkStyle") checkStyleList: List<CheckStyle>)

}