package com.thoughtworks.archguard.qualitygate.infrastructure

import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.util.*

class DateMapper : ColumnMapper<Date> {

    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): Date {
        return Date(r.getTimestamp(columnNumber).time)
    }

}
