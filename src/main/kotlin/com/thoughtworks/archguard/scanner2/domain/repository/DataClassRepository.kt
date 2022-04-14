package com.thoughtworks.archguard.scanner2.domain.repository

import com.thoughtworks.archguard.scanner2.domain.model.JClass

interface DataClassRepository {
    fun insertOrUpdateDataClass(systemId: Long, dataClasses: List<JClass>)
}
