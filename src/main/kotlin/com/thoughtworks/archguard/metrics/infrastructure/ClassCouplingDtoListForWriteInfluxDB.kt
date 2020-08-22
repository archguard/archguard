package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling

data class ClassCouplingDtoListForWriteInfluxDB(val classCouplings: List<ClassCoupling>) {
    fun toRequestBody() = classCouplings.joinToString("\n") { ClassCouplingDtoForWriteInfluxDB.fromClassCoupling(it).toInfluxDBRequestBody() }
}

data class ClassCouplingDtoForWriteInfluxDB(val classCoupling: ClassCoupling) {
    fun toInfluxDBRequestBody() : String {
        return "class_coupling,class_name=${classCoupling.jClassVO.name},package_name=${classCoupling.jClassVO.getPackageName()},module_name=${classCoupling.jClassVO.module} " +
                "inner_fan_in=${classCoupling.innerFanIn},inner_fan_out=${classCoupling.innerFanOut},outer_fan_in=${classCoupling.outerFanIn},outer_fan_out=${classCoupling.outerFanOut}," +
                "inner_instability=${classCoupling.innerInstability},inner_coupling=${classCoupling.innerCoupling},outer_instability=${classCoupling.outerInstability}," +
                "outer_coupling=${classCoupling.outerCoupling}"
    }

    companion object {
        fun fromClassCoupling(classCoupling: ClassCoupling): ClassCouplingDtoForWriteInfluxDB {
            return ClassCouplingDtoForWriteInfluxDB(classCoupling)
        }
    }
}