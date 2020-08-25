package com.thoughtworks.archguard.metrics.domain

import com.thoughtworks.archguard.metrics.domain.abstracts.ClassAbstractRatio
import com.thoughtworks.archguard.metrics.domain.abstracts.ModuleAbstractRatio
import com.thoughtworks.archguard.metrics.domain.abstracts.PackageAbstractRatio
import com.thoughtworks.archguard.metrics.domain.coupling.ModuleMetricsLegacy
import com.thoughtworks.archguard.metrics.domain.dfms.ClassDfms
import com.thoughtworks.archguard.metrics.domain.dfms.ModuleDfms
import com.thoughtworks.archguard.metrics.domain.dfms.PackageDfms
import com.thoughtworks.archguard.module.domain.graph.GraphStore
import com.thoughtworks.archguard.module.domain.model.JClassVO
import com.thoughtworks.archguard.module.domain.model.PackageVO

interface MetricsService {
    @Deprecated("")
    fun calculateCouplingLegacy()

    @Deprecated("")
    fun getAllMetricsLegacy(): List<ModuleMetricsLegacy>

    @Deprecated("")
    fun getModuleMetricsLegacy(): List<ModuleMetricsLegacy>

    fun getClassAbstractMetric(jClassVO: JClassVO): ClassAbstractRatio
    fun getPackageAbstractMetric(packageVO: PackageVO): PackageAbstractRatio
    fun getModuleAbstractMetric(moduleName: String): ModuleAbstractRatio

    fun getClassNoc(jClassVO: JClassVO): Int

    fun getClassDit(jClassVO: JClassVO): Int

    fun getClassAbc(jClassVO: JClassVO): Int

    fun getClassDfms(jClassVO: JClassVO): ClassDfms
    fun getPackageDfms(packageVO: PackageVO): PackageDfms
    fun getModuleDfms(moduleName: String): ModuleDfms

    fun getClassLCOM4(jClassVO: JClassVO): GraphStore

    fun calculateAllNoc(): List<ClassNoc>
    fun calculateAllDit(): List<ClassDit>
    fun calculateAllAbc(): List<ClassAbc>
    fun calculateAllLCOM4(): List<ClassLCOM4>
    fun calculateAllClassDfms(): List<ClassDfms>
    fun calculateAllModuleDfms(): List<ModuleDfms>

}