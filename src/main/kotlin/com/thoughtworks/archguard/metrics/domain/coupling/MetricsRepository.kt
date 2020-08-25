package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.module.domain.model.JClassVO

interface MetricsRepository {
    @Deprecated("")
    fun insert(moduleMetrics: List<ModuleMetricsLegacy>)

    @Deprecated("")
    fun findAllMetrics(moduleNames: List<String>): List<ModuleMetricsLegacy>

    @Deprecated("")
    fun findModuleMetrics(moduleNames: List<String>): List<ModuleMetricsLegacy>

    fun insertAllClassCouplings(classCouplings: List<ClassCoupling>)
    fun getClassCoupling(jClassVO: JClassVO): ClassCoupling?
    fun getClassCoupling(jClassVOs: List<JClassVO>): List<ClassCoupling>
}