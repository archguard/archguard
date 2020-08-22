package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling

data class ClassCouplingDtoListForWriteInfluxDB(val classCouplings: List<ClassCoupling>) {
    fun toRequestBody() = classCouplings.joinToString("\n") { ClassCouplingDtoForWriteInfluxDB.fromClassCoupling(it).toInfluxDBRequestBody() }
}

data class ClassCouplingDtoForWriteInfluxDB(val classCouplingNew: ClassCoupling,
                                            val className: String,
                                            val packageName: String,
                                            val moduleName: String,
                                            val innerFanIn: Int,
                                            val innerFanOut: Int,
                                            val outerFanIn: Int,
                                            val outerFanOut: Int,
                                            val innerInstability: Double,
                                            val innerCoupling: Double,
                                            val outerInstability: Double,
                                            val outerCoupling: Double) {
    fun toInfluxDBRequestBody() : String {
        return "class_coupling,class_name=${classCouplingNew.jClassVO.name},package_name=${classCouplingNew.jClassVO.getPackageName()},module_name=${classCouplingNew.jClassVO.module} " +
                "inner_fan_in=${classCouplingNew.innerFanIn},inner_fan_out=${classCouplingNew.innerFanOut},outer_fan_in=${classCouplingNew.outerFanIn},outer_fan_out=${classCouplingNew.outerFanOut}," +
                "inner_instability=${classCouplingNew.innerInstability},inner_coupling=${classCouplingNew.innerCoupling},outer_instability=${classCouplingNew.outerInstability}," +
                "outer_coupling=${classCouplingNew.outerCoupling}"
    }

    companion object {
        fun fromClassCoupling(classCoupling: ClassCoupling): ClassCouplingDtoForWriteInfluxDB {
            return ClassCouplingDtoForWriteInfluxDB(classCoupling, classCoupling.jClassVO.name, classCoupling.jClassVO.getPackageName(),
                    classCoupling.jClassVO.module, classCoupling.innerFanIn, classCoupling.innerFanOut,
                    classCoupling.outerFanIn, classCoupling.outerFanOut, classCoupling.innerInstability, classCoupling.innerCoupling,
                    classCoupling.outerInstability, classCoupling.outerCoupling)
        }
    }
}