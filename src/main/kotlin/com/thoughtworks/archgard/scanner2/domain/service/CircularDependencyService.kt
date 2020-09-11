package com.thoughtworks.archgard.scanner2.domain.service

import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.repository.JClassRepository
import org.springframework.stereotype.Service

@Service
class CircularDependencyService(val jClassRepository: JClassRepository) {
    fun getClassCircularDependency(systemId: String): List<List<JClassVO>> {

        return emptyList()
    }
}