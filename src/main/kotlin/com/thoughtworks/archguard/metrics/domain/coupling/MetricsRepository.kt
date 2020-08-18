package com.thoughtworks.archguard.metrics.domain.coupling

import com.thoughtworks.archguard.module.domain.model.JClassVO

interface MetricsRepository {
    fun insert(moduleMetrics: List<ModuleMetricsLegacy>)
    fun findAllMetrics(moduleNames: List<String>): List<ModuleMetricsLegacy>
    fun findModuleMetrics(moduleNames: List<String>): List<ModuleMetricsLegacy>

    fun insertAllClassCouplings(classCouplings: List<ClassCoupling>)
    fun getClassCoupling(jClassVO: JClassVO): ClassCoupling?
    fun getClassCoupling(jClassVOs: List<JClassVO>): List<ClassCoupling>?
}