package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import com.thoughtworks.archguard.code.module.domain.model.JClassVO

interface MetricsRepository {

    fun insertAllClassCouplings(systemId:Long, classCouplings: List<ClassCoupling>)
    fun getClassCoupling(jClassVO: JClassVO): ClassCoupling?
    fun getClassCoupling(jClassVOs: List<JClassVO>): List<ClassCoupling>
}
