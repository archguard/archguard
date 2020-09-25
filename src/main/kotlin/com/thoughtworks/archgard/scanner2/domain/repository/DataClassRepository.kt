package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.JClass

interface DataClassRepository {
    fun insertOrUpdateDataClass(systemId: Long, dataClasses: List<JClass>)
}