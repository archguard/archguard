package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.model.JMethodVO

interface CircularDependencyMetricRepository {
    fun insertOrUpdateClassCircularDependency(systemId: Long, classCircularDependency: List<List<JClassVO>>)
    fun insertOrUpdateMethodCircularDependency(systemId: Long, methodCircularDependency: List<List<JMethodVO>>)
    fun insertOrUpdateModuleCircularDependency(systemId: Long, moduleCircularDependency: List<List<String>>)
    fun insertOrUpdatePackageCircularDependency(systemId: Long, packageCircularDependency: List<List<String>>)
}