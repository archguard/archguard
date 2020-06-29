package com.thoughtworks.archguard.module.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.nield.kotlinstatistics.median
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class LogicModuleService {
    private val log = LoggerFactory.getLogger(LogicModuleService::class.java)

    @Autowired
    lateinit var logicModuleRepository: LogicModuleRepository

    @Autowired
    lateinit var baseModuleRepository: BaseModuleRepository

    fun getLogicModules(): List<LogicModule> {
        return logicModuleRepository.getAll()
    }

    fun hideAllLogicModules() {
        val logicModules = getLogicModules()
        logicModules.forEach { it.hide() }
        logicModuleRepository.updateAll(logicModules)
    }

    fun showAllLogicModules() {
        val logicModules = getLogicModules()
        logicModules.forEach { it.show() }
        logicModuleRepository.updateAll(logicModules)
    }

    fun reverseAllLogicModulesStatus() {
        val logicModules = getLogicModules()
        logicModules.forEach { it.reverse() }
        logicModuleRepository.updateAll(logicModules)
    }

    fun updateLogicModule(id: String, logicModule: LogicModule) {
        logicModuleRepository.update(id, logicModule)
    }

    fun createLogicModule(logicModule: LogicModule): String {
        val id = UUID.randomUUID().toString()
        logicModule.id = id
        logicModuleRepository.create(logicModule)
        return id
    }

    fun deleteLogicModule(id: String) {
        logicModuleRepository.delete(id)
    }

    fun getLogicModulesDependencies(caller: String, callee: String): List<ModuleDependency> {
        return logicModuleRepository.getDependence(caller, callee)
    }

    fun autoDefineLogicModule() {
        logicModuleRepository.deleteAll()
        val defaultModules = baseModuleRepository.getBaseModules()
                .map { LogicModule(UUID.randomUUID().toString(), it, mutableListOf(it)) }
        logicModuleRepository.saveAll(defaultModules)
    }

    fun autoDefineLogicModuleWithInterface() {
        logicModuleRepository.deleteAll()
        val jClassesHasModules: List<JClass> = baseModuleRepository.getJClassesHasModules()
        val defineLogicModuleWithInterface = getLogicModulesForAllJClass(jClassesHasModules)
        logicModuleRepository.saveAll(defineLogicModuleWithInterface)
    }

    internal fun getLogicModulesForAllJClass(jClassesHasModules: List<JClass>): List<LogicModule> {
        return jClassesHasModules
                .map { getLogicModuleForJClass(it) }
                .groupBy({ it.name }, { it.members })
                .mapValues { entry -> entry.value.flatten().toSet().toList() }
                .map { LogicModule(UUID.randomUUID().toString(), it.key, it.value) }
    }

    internal fun getLogicModuleForJClass(jClass: JClass): LogicModule {
        val (id, _, moduleName) = jClass
        val parentClassIds = logicModuleRepository.getParentClassId(id)
        val moduleNames = parentClassIds.asSequence().map { id -> baseModuleRepository.getJClassesById(id) }
                .filter { j -> j.module != "null" }
                .filter { j -> j.module != jClass.module }
                .map { j -> j.module + "." + j.name }
                .toSet().toMutableList()
        moduleNames.add(moduleName)
        return LogicModule(null, moduleName, moduleNames)
    }

    fun getLogicModuleGraph(): ModuleGraph {
        val results = getModuleDependency()

        val moduleStore = ModuleStore()
        results
                .groupBy { it.caller }
                .forEach {
                    it.value.groupBy { i -> i.callee }
                            .forEach { i -> moduleStore.addEdge(it.key, i.key, i.value.size) }
                }

        return moduleStore.getModuleGraph()
    }

    private fun getModuleDependency(): List<Dependency> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val members = modules.map { it.members }.flatten()
        val classDependencies = logicModuleRepository.getAllClassDependency(members)
        return mapClassDependencyToModuleDependency(classDependencies, modules)
    }

    fun mapClassDependencyToModuleDependency(classDependency: List<Dependency>, modules: List<LogicModule>): List<Dependency> {
        // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
        return classDependency.map {
            val callerModules = getModule(modules, it.caller)
            val calleeModules = getModule(modules, it.callee)
            callerModules.flatMap { callerModule -> calleeModules.map { calleeModule -> callerModule to calleeModule } }
                    .map { it -> Dependency(it.first, it.second) }
        }.flatten().filter { it.caller != it.callee }
    }

    fun getModule(modules: List<LogicModule>, name: String): List<String> {
        val callerByFullMatch = fullMatch(name, modules)
        if (callerByFullMatch.isNotEmpty()) {
            return callerByFullMatch
        }
        return startsWithMatch(name, modules)
    }

    private fun fullMatch(name: String, modules: List<LogicModule>): List<String> {
        return modules.filter { logicModule ->
            logicModule.members.any { javaClass -> name == javaClass }
        }.map { it.name }
    }

    private fun startsWithMatch(name: String, modules: List<LogicModule>): List<String> {
        var maxMatchSize = 0
        val matchModule: MutableList<String> = mutableListOf()
        for (logicModule in modules) {
            val maxMatchSizeInLogicModule = logicModule.members
                    .filter { member -> name.startsWith("$member.") }
                    .maxBy { it.length }
                    ?: continue
            if (maxMatchSizeInLogicModule.length > maxMatchSize) {
                maxMatchSize = maxMatchSizeInLogicModule.length
                matchModule.clear()
                matchModule.add(logicModule.name)
            } else if (maxMatchSizeInLogicModule.length == maxMatchSize) {
                matchModule.add(logicModule.name)
            }
        }
        if (matchModule.isEmpty()) {
            log.error("{} No LogicModule matched!", name)
            throw RuntimeException("$name No LogicModule matched!")
        }
        return matchModule.toList()
    }

    fun getLogicModuleCouplingReport(): List<ModuleCouplingReportDTO> {
        return getLogicModuleCouplingReportDetail().map { ModuleCouplingReportDTO(it) }
    }

    fun getLogicModuleCouplingReportDetail(): List<ModuleCouplingReport> {
        val modules = logicModuleRepository.getAllByShowStatus(true)
        val members = modules.map { it.members }.flatten()
        val classDependency = logicModuleRepository.getAllClassDependency(members)

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
            val reportRelatedModules = getModule(modules, packageCouplingReport.packageName)
            for (module in reportRelatedModules) {
                if (packageCouplingReportMap.containsKey(module)) {
                    packageCouplingReportMap[module]?.add(packageCouplingReport)
                } else {
                    packageCouplingReportMap[module] = mutableListOf(packageCouplingReport)
                }
            }
        }
        log.info("Group package to module done.")
        return packageCouplingReportMap.map { ModuleCouplingReport(it.key, it.value) }
    }

    private fun getClassCouplingReports(dependency: List<Dependency>,
                                        modules: List<LogicModule>): List<ClassCouplingReport> =
            dependency.flatMap { listOf(it.callee, it.caller) }.distinct()
                    .map { getClassCouplingReport(it, dependency, modules) }

    fun getClassCouplingReport(clazz: String,
                               dependency: List<Dependency>,
                               modules: List<LogicModule>): ClassCouplingReport {
        val innerFanIn = dependency.filter { it.callee == clazz }.filter { isInSameModule(modules, it) }.count()
        val innerFanOut = dependency.filter { it.caller == clazz }.filter { isInSameModule(modules, it) }.count()
        val outerFanIn = dependency.filter { it.callee == clazz }.filter { !isInSameModule(modules, it) }.count()
        val outerFanOut = dependency.filter { it.caller == clazz }.filter { !isInSameModule(modules, it) }.count()
        return ClassCouplingReport(clazz, innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }

    private fun isInSameModule(modules: List<LogicModule>, it: Dependency): Boolean {
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
