package com.thoughtworks.archguard.module.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.LogicModule
import com.thoughtworks.archguard.module.domain.model.SubModule
import org.nield.kotlinstatistics.median
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReportServiceImpl : ReportService {
    private val log = LoggerFactory.getLogger(ReportServiceImpl::class.java)

    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    @Autowired
    lateinit var jClassRepository: JClassRepository

    override fun getLogicModuleCouplingReport(): List<ModuleCouplingReportDTO> {
        return getLogicModuleCouplingReportDetail().map { ModuleCouplingReportDTO(it) }
    }

    override fun getLogicModuleCouplingReportDetail(): List<ModuleCouplingReport> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val members = modules.map { it.members }.flatten()
        val classDependency = jClassRepository.getAllClassDependency(members)

        val classCouplingReports = getClassCouplingReports(classDependency, modules)
        log.info("Get class Coupling reports done.")
        return groupPackageCouplingReportsByModuleName(groupToPackage(classCouplingReports), modules)
    }

    fun groupToPackage(classCouplingReports: List<ClassCouplingReport>): List<PackageCouplingReport> {
        val classCouplingReportMap: MutableMap<String, MutableList<ClassCouplingReport>> = mutableMapOf()
        for (classCouplingReport in classCouplingReports) {
            val packageName = getPackageByClassName(classCouplingReport.className)
            if (classCouplingReportMap.containsKey(packageName)) {
                classCouplingReportMap[packageName]?.add(classCouplingReport)
            } else {
                classCouplingReportMap[packageName] = mutableListOf(classCouplingReport)
            }
        }
        log.info("Group class to package done.")
        return classCouplingReportMap.map { PackageCouplingReport(it.key, it.value) }
    }

    private fun getPackageByClassName(clazz: String): String {
        return clazz.substringBeforeLast('.')
    }


    fun groupPackageCouplingReportsByModuleName(packageCouplingReports: List<PackageCouplingReport>,
                                                modules: List<LogicModule>): List<ModuleCouplingReport> {
        val packageCouplingReportMap: MutableMap<String, MutableList<PackageCouplingReport>> = mutableMapOf()
        for (packageCouplingReport in packageCouplingReports) {
            val reportRelatedModules = getModule(modules, SubModule(packageCouplingReport.packageName))
            for (module in reportRelatedModules) {
                if (packageCouplingReportMap.containsKey(module.name)) {
                    packageCouplingReportMap[module.name]?.add(packageCouplingReport)
                } else {
                    packageCouplingReportMap[module.name] = mutableListOf(packageCouplingReport)
                }
            }
        }
        log.info("Group package to module done.")
        return packageCouplingReportMap.map { ModuleCouplingReport(it.key, it.value) }
    }

    private fun getClassCouplingReports(dependency: List<Dependency<JClass>>,
                                        modules: List<LogicModule>): List<ClassCouplingReport> =
            dependency.flatMap { listOf(it.callee, it.caller) }.distinct()
                    .map { getClassCouplingReport(it, dependency, modules) }

    fun getClassCouplingReport(clazz: JClass,
                               dependency: List<Dependency<JClass>>,
                               modules: List<LogicModule>): ClassCouplingReport {
        val innerFanIn = dependency.filter { it.callee == clazz }.filter { isInSameModule(modules, it) }.count()
        val innerFanOut = dependency.filter { it.caller == clazz }.filter { isInSameModule(modules, it) }.count()
        val outerFanIn = dependency.filter { it.callee == clazz }.filter { !isInSameModule(modules, it) }.count()
        val outerFanOut = dependency.filter { it.caller == clazz }.filter { !isInSameModule(modules, it) }.count()
        return ClassCouplingReport(clazz.getFullName(), innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }

    private fun isInSameModule(modules: List<LogicModule>, it: Dependency<JClass>): Boolean {
        val callerModules = getModule(modules, it.caller)
        val calleeModules = getModule(modules, it.callee)
        return callerModules.intersect(calleeModules).isNotEmpty()
    }
}


data class ModuleCouplingReportDTO(@JsonIgnore val moduleCouplingReport: ModuleCouplingReport) {
    val moduleName = moduleCouplingReport.moduleName
    val outerModuleInstabilityAverage: Double = moduleCouplingReport.outerModuleInstabilityAverage
    val outerModuleInstabilityMedian: Double = moduleCouplingReport.outerModuleInstabilityMedian
    val outerModuleCouplingAverage: Double = moduleCouplingReport.outerModuleCouplingAverage
    val outerModuleCouplingMedian: Double = moduleCouplingReport.outerModuleCouplingMedian
    val innerModuleInstabilityAverage: Double = moduleCouplingReport.innerModuleInstabilityAverage
    val innerModuleInstabilityMedian: Double = moduleCouplingReport.innerModuleInstabilityMedian
    val innerModuleCouplingAverage: Double = moduleCouplingReport.innerModuleCouplingAverage
    val innerModuleCouplingMedian: Double = moduleCouplingReport.innerModuleCouplingMedian
}

data class PackageCouplingReport(val packageName: String,
                                 val classCouplingReports: List<ClassCouplingReport>) {
    val outerPackageInstabilityAverage: Double = classCouplingReports.map { it.outerClassInstability }.average()
    val outerPackageInstabilityMedian: Double = classCouplingReports.map { it.outerClassInstability }.median()
    val outerPackageCouplingAverage: Double = classCouplingReports.map { it.outerClassCoupling }.average()
    val outerPackageCouplingMedian: Double = classCouplingReports.map { it.outerClassCoupling }.median()
    val innerPackageInstabilityAverage: Double = classCouplingReports.map { it.innerClassInstability }.average()
    val innerPackageInstabilityMedian: Double = classCouplingReports.map { it.innerClassInstability }.median()
    val innerPackageCouplingAverage: Double = classCouplingReports.map { it.innerClassCoupling }.average()
    val innerPackageCouplingMedian: Double = classCouplingReports.map { it.innerClassCoupling }.median()

}

data class ModuleCouplingReport(val moduleName: String,
                                val packageCouplingReports: List<PackageCouplingReport>) {
    val outerModuleInstabilityAverage: Double = packageCouplingReports.flatMap { it.classCouplingReports }.map { it.outerClassInstability }.average()
    val outerModuleInstabilityMedian: Double = packageCouplingReports.flatMap { it.classCouplingReports }.map { it.outerClassInstability }.median()
    val outerModuleCouplingAverage: Double = packageCouplingReports.flatMap { it.classCouplingReports }.map { it.outerClassCoupling }.average()
    val outerModuleCouplingMedian: Double = packageCouplingReports.flatMap { it.classCouplingReports }.map { it.outerClassCoupling }.median()
    val innerModuleInstabilityAverage: Double = packageCouplingReports.flatMap { it.classCouplingReports }.map { it.innerClassInstability }.average()
    val innerModuleInstabilityMedian: Double = packageCouplingReports.flatMap { it.classCouplingReports }.map { it.innerClassInstability }.median()
    val innerModuleCouplingAverage: Double = packageCouplingReports.flatMap { it.classCouplingReports }.map { it.innerClassCoupling }.average()
    val innerModuleCouplingMedian: Double = packageCouplingReports.flatMap { it.classCouplingReports }.map { it.innerClassCoupling }.median()

}

data class ClassCouplingReport(val className: String,
                               val innerFanIn: Int,
                               val innerFanOut: Int,
                               val outerFanIn: Int,
                               val outerFanOut: Int) {
    val innerClassInstability: Double = if (innerFanIn + innerFanOut == 0) 0.0 else innerFanOut.toDouble() / (innerFanOut + innerFanIn)
    val innerClassCoupling: Double = if (innerFanIn + innerFanOut == 0) 0.0 else 1 - 1.0 / (innerFanOut + innerFanIn)
    val outerClassInstability: Double = if (outerFanIn + outerFanOut == 0) 0.0 else outerFanOut.toDouble() / (outerFanOut + outerFanIn)
    val outerClassCoupling: Double = if (outerFanIn + outerFanOut == 0) 0.0 else 1 - 1.0 / (outerFanOut + outerFanIn)
}