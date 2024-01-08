package com.thoughtworks.archguard.scanner2.domain.repository

import org.archguard.model.vos.JClassVO
import org.archguard.model.vos.JMethodVO

interface ScannerCircularDependencyMetricRepository {
    fun insertOrUpdateClassCircularDependency(systemId: Long, classCircularDependency: List<List<JClassVO>>)
    fun insertOrUpdateMethodCircularDependency(systemId: Long, methodCircularDependency: List<List<JMethodVO>>)
    fun insertOrUpdateModuleCircularDependency(systemId: Long, moduleCircularDependency: List<List<String>>)
    fun insertOrUpdatePackageCircularDependency(systemId: Long, packageCircularDependency: List<List<String>>)
}
