package com.thoughtworks.archguard.metrics.infrastructure.influx

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling

data class ClassCouplingListInfluxDTO(val systemId: Long, val classCouplings: List<ClassCoupling>) {
    fun toRequestBody() = classCouplings.joinToString("\n") { ClassCouplingInfluxDTO(systemId, it).toInfluxDBRequestBody() }
}

data class ClassCouplingInfluxDTO(val systemId: Long, val classCoupling: ClassCoupling) {
    fun toInfluxDBRequestBody(): String {
        return "metric_class_coupling,class_name=${classCoupling.jClassVO.name},package_name=${classCoupling.jClassVO.getPackageName()},module_name=${classCoupling.jClassVO.module},system_id=$systemId " +
            "inner_fan_in=${classCoupling.innerFanIn},inner_fan_out=${classCoupling.innerFanOut}," +
            "outer_fan_in=${classCoupling.outerFanIn},outer_fan_out=${classCoupling.outerFanOut}," +
            "inner_instability=${classCoupling.innerInstability},inner_coupling=${classCoupling.innerCoupling}," +
            "outer_instability=${classCoupling.outerInstability},outer_coupling=${classCoupling.outerCoupling}"
    }
}
