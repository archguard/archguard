package com.thoughtworks.archguard.change.infrastructure

import com.thoughtworks.archguard.change.domain.DiffChange
import org.jdbi.v3.sqlobject.statement.SqlQuery

interface DiffChangeDao {
    @SqlQuery("select system_id as systemId, since_rev as sinceRev, until_rev as untilRev, package_name as packageName, class_name as className, relations  from scm_diff_change where system_id = :systemId")
    fun findBySystemId(systemId: Long) : List<DiffChange>
}
