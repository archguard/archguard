package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.model.code.JClass

interface DataClassRepository {
    fun insertOrUpdateDataClass(systemId: Long, dataClasses: List<JClass>)
}
