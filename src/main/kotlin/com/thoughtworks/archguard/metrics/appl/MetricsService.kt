package com.thoughtworks.archguard.metrics.appl

import com.thoughtworks.archguard.metrics.domain.ClassAbc
import com.thoughtworks.archguard.metrics.domain.ClassDit
import com.thoughtworks.archguard.metrics.domain.ClassLCOM4
import com.thoughtworks.archguard.metrics.domain.ClassNoc
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
    fun calculateCouplingLegacy(systemId: Long)

    @Deprecated("")
    fun getAllMetricsLegacy(systemId: Long): List<ModuleMetricsLegacy>

    fun getClassAbstractMetric(systemId: Long, jClassVO: JClassVO): ClassAbstractRatio
    fun getPackageAbstractMetric(systemId: Long, packageVO: PackageVO): PackageAbstractRatio
    fun getModuleAbstractMetric(systemId: Long, moduleName: String): ModuleAbstractRatio

    fun getClassNoc(systemId: Long, jClassVO: JClassVO): Int
    fun getClassDit(systemId: Long, jClassVO: JClassVO): Int
    fun getClassLCOM4(systemId: Long, jClassVO: JClassVO): GraphStore
    fun getClassAbc(systemId: Long, jClassVO: JClassVO): Int

    fun getClassDfms(systemId: Long, jClassVO: JClassVO): ClassDfms
    fun calculateAllClassDfms(systemId: Long): List<ClassDfms>
    fun getPackageDfms(systemId: Long, packageVO: PackageVO): PackageDfms
    fun getModuleDfms(systemId: Long, moduleName: String): ModuleDfms
    fun calculateAllModuleDfms(systemId: Long): List<ModuleDfms>

}
