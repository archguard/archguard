package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling

data class MetricClassCouplingWritePO(
    val classId: String,
    val systemId: Long,
    val innerFanIn: Int,
    val innerFanOut: Int,
    val outerFanIn: Int,
    val outerFanOut: Int,
    val innerInstability: Double,
    val innerCoupling: Double,
    val outerInstability: Double,
    val outerCoupling: Double
) {
    companion object {
        fun fromClassCoupling(systemId: Long, classCoupling: ClassCoupling): MetricClassCouplingWritePO {
            return MetricClassCouplingWritePO(
                classCoupling.jClassVO.id!!, systemId, classCoupling.innerFanIn, classCoupling.innerFanOut,
                classCoupling.outerFanIn, classCoupling.outerFanOut, classCoupling.innerInstability, classCoupling.innerCoupling,
                classCoupling.outerInstability, classCoupling.outerCoupling
            )
        }
    }
}
