package com.thoughtworks.archguard.scanner2.domain.repository

import com.thoughtworks.archguard.scanner2.domain.model.ChangeEntry

interface ChangeEntryRepository {
    fun getAllChangeEntry(systemId: Long): List<ChangeEntry>
}