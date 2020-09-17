package com.thoughtworks.archgard.scanner2.infrastructure.influx

import com.thoughtworks.archgard.scanner2.domain.model.CircularDependenciesCount

data class CircularDependenciesCountDtoForWriteInfluxDB(val circularDependenciesCount: CircularDependenciesCount) {
    fun toRequestBody(): String {
        return "circular_dependencies_count," +
                "module=${circularDependenciesCount.moduleCircularDependenciesCount}," +
                "package=${circularDependenciesCount.packageCircularDependenciesCount}," +
                "class=${circularDependenciesCount.classCircularDependenciesCount}," +
                "method=${circularDependenciesCount.methodCircularDependenciesCount}," +
                "system_id=${circularDependenciesCount.systemId}"
    }

}