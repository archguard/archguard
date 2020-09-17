package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.CircularDependenciesCount

data class CircularDependenciesCountDtoForWriteInfluxDB(val circularDependenciesCount: CircularDependenciesCount) {
    fun toRequestBody(): String {
        return "circular_dependencies_count,system_id=${circularDependenciesCount.systemId} " +
                "module=${circularDependenciesCount.moduleCircularDependenciesCount}," +
                "package=${circularDependenciesCount.packageCircularDependenciesCount}," +
                "class=${circularDependenciesCount.classCircularDependenciesCount}," +
                "method=${circularDependenciesCount.methodCircularDependenciesCount}"
    }

}