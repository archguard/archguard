package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.ChangeEntry
import com.thoughtworks.archgard.scanner2.domain.repository.ChangeEntryRepository
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.reflect.ConstructorMapper
import org.springframework.stereotype.Repository

@Repository
class ChangeEntryRepositoryImpl(val jdbi: Jdbi) : ChangeEntryRepository {
    override fun getAllChangeEntry(systemId: Long): List<ChangeEntry> {
        val sql = "SELECT * from change_entry WHERE system_id = $systemId"
        return jdbi.withHandle<List<ChangeEntryPO>, Nothing> {
            it.registerRowMapper(ConstructorMapper.factory(ChangeEntryPO::class.java))
            it.createQuery(sql)
                    .mapTo(ChangeEntryPO::class.java)
                    .list()
        }.map { it.toChangeEntry() }
    }
}