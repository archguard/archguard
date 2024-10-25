package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.context.ChangeEntry

interface ChangeEntryRepository {
    fun getAllChangeEntry(systemId: Long): List<ChangeEntry>
}
