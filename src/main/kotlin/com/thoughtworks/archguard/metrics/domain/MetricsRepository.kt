package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.code.module.domain.model.JClassVO
import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling

interface MetricsRepository {

    fun insertAllClassCouplings(systemId: Long, classCouplings: List<ClassCoupling>)
    fun getClassCoupling(jClassVO: JClassVO): ClassCoupling?
    fun getClassCoupling(jClassVOs: List<JClassVO>): List<ClassCoupling>
}
