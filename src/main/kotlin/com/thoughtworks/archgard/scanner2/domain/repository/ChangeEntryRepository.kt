package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.ChangeEntry

interface ChangeEntryRepository {
    fun getAllChangeEntry(systemId: Long): List<ChangeEntry>
}