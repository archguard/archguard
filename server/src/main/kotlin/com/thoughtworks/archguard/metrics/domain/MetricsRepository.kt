package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.metrics.domain.coupling.ClassCoupling
import org.archguard.model.vos.JClassVO

interface MetricsRepository {

    fun insertAllClassCouplings(systemId: Long, classCouplings: List<ClassCoupling>)
    fun getClassCoupling(jClassVO: JClassVO): ClassCoupling?
    fun getClassCoupling(jClassVOs: List<JClassVO>): List<ClassCoupling>
}
