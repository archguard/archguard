package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.model.ChangeEntry

interface ChangeEntryRepository {
    fun getAllChangeEntry(systemId: Long): List<ChangeEntry>
}
