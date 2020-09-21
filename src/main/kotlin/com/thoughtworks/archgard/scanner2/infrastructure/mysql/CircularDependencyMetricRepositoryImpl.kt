package com.thoughtworks.archgard.scanner2.infrastructure.mysql

import com.thoughtworks.archgard.scanner2.domain.model.JClassVO
import com.thoughtworks.archgard.scanner2.domain.model.JMethodVO
import com.thoughtworks.archgard.scanner2.domain.repository.CircularDependencyMetricRepository
import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository

@Repository
class CircularDependencyMetricRepositoryImpl(val jdbi: Jdbi) : CircularDependencyMetricRepository {
    override fun insertOrUpdateClassCircularDependency(systemId: Long, classCircularDependency: List<List<JClassVO>>) {
        deleteCircularDependency(systemId, type = CircularDependencyType.CLASS)
        classCircularDependency.forEach {
            val circularDependency = it.joinToString(separator = ";") { it.fullName }
            saveCircularDependency(systemId, circularDependency, CircularDependencyType.CLASS)
        }
    }

    override fun insertOrUpdateMethodCircularDependency(systemId: Long, methodCircularDependency: List<List<JMethodVO>>) {
        deleteCircularDependency(systemId, type = CircularDependencyType.METHOD)
        methodCircularDependency.forEach {
            val circularDependency = it.joinToString(separator = ";") { it.fullName }
            saveCircularDependency(systemId, circularDependency, CircularDependencyType.METHOD)
        }
    }

    override fun insertOrUpdateModuleCircularDependency(systemId: Long, moduleCircularDependency: List<List<String>>) {
        deleteCircularDependency(systemId, type = CircularDependencyType.MODULE)
        moduleCircularDependency.forEach {
            val circularDependency = it.joinToString(separator = ";")
            saveCircularDependency(systemId, circularDependency, CircularDependencyType.MODULE)
        }
    }

    override fun insertOrUpdatePackageCircularDependency(systemId: Long, packageCircularDependency: List<List<String>>) {
        deleteCircularDependency(systemId, type = CircularDependencyType.PACKAGE)
        packageCircularDependency.forEach {
            val circularDependency = it.joinToString(separator = ";")
            saveCircularDependency(systemId, circularDependency, CircularDependencyType.PACKAGE)
        }
    }

    fun saveCircularDependency(systemId: Long, circularDependency: String, type: CircularDependencyType) {
        jdbi.useHandle<Exception> {
            it.createUpdate("insert into circular_dependency_metrics (system_id, circular_dependency, type) values (:system_id, :circular_dependency, :type)")
                    .bind("system_id", systemId)
                    .bind("circular_dependency", circularDependency)
                    .bind("type", type)
                    .execute()
        }
    }

    fun deleteCircularDependency(systemId: Long, type: CircularDependencyType) {
        jdbi.useHandle<Exception> {
            it.createUpdate("delete from circular_dependency_metrics where system_id=:system_id and type=:type")
                    .bind("system_id", systemId)
                    .bind("type", type)
                    .execute()
        }
    }
}

enum class CircularDependencyType {
    CLASS, METHOD, PACKAGE, MODULE
}
