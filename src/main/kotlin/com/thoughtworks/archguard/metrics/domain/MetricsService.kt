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
    fun calculateCouplingLegacy(projectId:Long)

    @Deprecated("")
    fun getAllMetricsLegacy(projectId:Long): List<ModuleMetricsLegacy>

    @Deprecated("")
    fun getModuleMetricsLegacy(projectId:Long): List<ModuleMetricsLegacy>

    fun getClassAbstractMetric(projectId:Long, jClassVO: JClassVO): ClassAbstractRatio
    fun getPackageAbstractMetric(projectId:Long, packageVO: PackageVO): PackageAbstractRatio
    fun getModuleAbstractMetric(projectId:Long, moduleName: String): ModuleAbstractRatio

    fun getClassNoc(projectId:Long, jClassVO: JClassVO): Int

    fun getClassDit(projectId:Long, jClassVO: JClassVO): Int

    fun getClassAbc(projectId:Long, jClassVO: JClassVO): Int

    fun getClassDfms(projectId:Long, jClassVO: JClassVO): ClassDfms
    fun getPackageDfms(projectId:Long, packageVO: PackageVO): PackageDfms
    fun getModuleDfms(projectId:Long, moduleName: String): ModuleDfms

    fun getClassLCOM4(projectId:Long, jClassVO: JClassVO): GraphStore

    fun calculateAllNoc(projectId:Long): List<ClassNoc>
    fun calculateAllDit(projectId:Long): List<ClassDit>
    fun calculateAllAbc(projectId:Long): List<ClassAbc>
    fun calculateAllLCOM4(projectId:Long): List<ClassLCOM4>
    fun calculateAllClassDfms(projectId:Long): List<ClassDfms>
    fun calculateAllModuleDfms(projectId:Long): List<ModuleDfms>

}
