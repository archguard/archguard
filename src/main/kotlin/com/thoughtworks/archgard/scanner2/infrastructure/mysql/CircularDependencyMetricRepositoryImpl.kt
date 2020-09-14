package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.model.JMethodVO
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import org.springframework.stereotype.Repository

@Repository
class CircularDependencyMetricRepositoryImpl : CircularDependencyMetricRepository {
    override fun saveClassCircularDependency(classCircularDependency: List<List<JClassVO>>) {
        TODO("Not yet implemented")
    }

    override fun saveMethodCircularDependency(methodCircularDependency: List<List<JMethodVO>>) {
        TODO("Not yet implemented")
    }
}