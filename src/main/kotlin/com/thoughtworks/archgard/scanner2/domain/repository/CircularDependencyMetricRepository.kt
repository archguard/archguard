package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.model.JMethodVO

interface CircularDependencyMetricRepository {
    fun saveClassCircularDependency(classCircularDependency: List<List<JClassVO>>)
    fun saveMethodCircularDependency(methodCircularDependency: List<List<JMethodVO>>)
}