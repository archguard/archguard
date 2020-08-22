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
        return "class_coupling,class_name=$className,package_name=$packageName,module_name=$moduleName " +
                "inner_fan_in=$innerFanIn,inner_fan_out=$innerFanOut,outer_fan_in=$outerFanIn,outer_fan_out=$outerFanOut," +
                "inner_instability=${innerInstability.toBigDecimal().toPlainString()},inner_coupling=$innerCoupling,outer_instability=$outerInstability," +
                "outer_coupling=$outerCoupling"
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