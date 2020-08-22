package com.thoughtworks.archguard.metrics.infrastructure

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling

data class ClassCouplingDtoListForWriteInfluxDB(val classCouplings: List<ClassCoupling>) {
    fun toRequestString() = classCouplings.joinToString("\n") { ClassCouplingDtoForWriteInfluxDB.fromClassCoupling(it).toInfluxDBRequestBody() }
}