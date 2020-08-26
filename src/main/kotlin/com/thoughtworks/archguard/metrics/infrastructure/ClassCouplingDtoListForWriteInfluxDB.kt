package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling

data class ClassCouplingDtoListForWriteInfluxDB(val projectId: Long, val classCouplings: List<ClassCoupling>) {
    fun toRequestBody() = classCouplings.joinToString("\n") { ClassCouplingDtoForWriteInfluxDB(projectId, it).toInfluxDBRequestBody() }
}

data class ClassCouplingDtoForWriteInfluxDB(val projectId: Long, val classCoupling: ClassCoupling) {
    fun toInfluxDBRequestBody(): String {
        return "class_coupling,class_name=${classCoupling.jClassVO.name},package_name=${classCoupling.jClassVO.getPackageName()},module_name=${classCoupling.jClassVO.module},project_id=${projectId} " +
                "inner_fan_in=${classCoupling.innerFanIn},inner_fan_out=${classCoupling.innerFanOut}," +
                "outer_fan_in=${classCoupling.outerFanIn},outer_fan_out=${classCoupling.outerFanOut}," +
                "inner_instability=${classCoupling.innerInstability},inner_coupling=${classCoupling.innerCoupling}," +
                "outer_instability=${classCoupling.outerInstability},outer_coupling=${classCoupling.outerCoupling}"
    }
}