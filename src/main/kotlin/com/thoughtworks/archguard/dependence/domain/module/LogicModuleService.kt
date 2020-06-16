package com.thoughtworks.archguard.dependence.domain.module

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

    private fun getModuleDependency(): List<CallerCalleeCouple> {
        val modules = logicModuleRepository.getAll()
        val members = modules.map { it.members }.flatten()
        val classCallerCalleeCouple = logicModuleRepository.getAllCallerCalleeCoupleAtClassLevel(members)
        return mapClassCoupleToModuleCouple(classCallerCalleeCouple, modules)
    }

    fun mapClassCoupleToModuleCouple(classCallerCalleeCouple: List<CallerCalleeCouple>, modules: List<LogicModule>): List<CallerCalleeCouple> {
        // 一个接口有多个实现/父类有多个子类: 就多条依赖关系
        return classCallerCalleeCouple.map {
            val callerModules = getModuleByClassName(modules, it.caller)
            val calleeModules = getModuleByClassName(modules, it.callee)
            callerModules.flatMap { callerModule -> calleeModules.map { calleeModule -> callerModule to calleeModule } }
                    .map { it -> CallerCalleeCouple(it.first, it.second) }
        }.flatten().filter { it.caller != it.callee }
    }

    fun getModuleByClassName(modules: List<LogicModule>, className: String): List<String> {
        val callerByFullMatch = fullMatch(className, modules)
        if (callerByFullMatch.isNotEmpty()) {
            return callerByFullMatch
        }
        return startsWithMatch(className, modules)
    }

    private fun fullMatch(className: String, modules: List<LogicModule>): List<String> {
        return modules.filter { logicModule ->
            logicModule.members.any { javaClass -> className == javaClass }
        }.map { it.name }
    }

    private fun startsWithMatch(callerClass: String, modules: List<LogicModule>): List<String> {
        var maxMatchSize = 0
        val matchModule: MutableList<String> = mutableListOf()
        for (logicModule in modules) {
            val maxMatchSizeInLogicModule = logicModule.members
                    .filter { member -> callerClass.startsWith(member + ".") }
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
            log.error("{} No LogicModule matched!", callerClass)
            throw RuntimeException(callerClass + "No LogicModule matched!")
        }
        return matchModule.toList()
    }

    fun getLogicModuleCoupling(): List<ModuleCouplingReport> {
        val moduleDependency = getModuleDependency()
        return getLogicModules().map { getModuleCouplingReport(it, moduleDependency) }
    }

    fun getModuleCouplingReport(module: LogicModule,
                                moduleDependency: List<CallerCalleeCouple>): ModuleCouplingReport {
        val fanOut = moduleDependency.filter { it.caller == module.name }.count()
        val fanIn = moduleDependency.filter { it.callee == module.name }.count()
        return ModuleCouplingReport(module.name, fanIn, fanOut)
    }

    fun getLogicModuleCouplingByClass(): List<NewModuleCouplingReport> {
        val modules = logicModuleRepository.getAll()
        val members = modules.map { it.members }.flatten()
        val classCallerCalleeCouple = logicModuleRepository.getAllCallerCalleeCoupleAtClassLevel(members)

        val classCouplingReports = getClassCouplingReports(classCallerCalleeCouple, modules)
        log.info("Get class Coupling reports done.")
        return groupClassCouplingReportsByModuleName(classCouplingReports, modules)
                .map { NewModuleCouplingReport(it.key, it.value) }
    }

    fun groupClassCouplingReportsByModuleName(classCouplingReports: List<ClassCouplingReport>,
                                              modules: List<LogicModule>): MutableMap<String, MutableList<ClassCouplingReport>> {
        val classCouplingReportMap: MutableMap<String, MutableList<ClassCouplingReport>> = mutableMapOf()
        for (classCouplingReport in classCouplingReports) {
            val reportRelatedModules = getModuleByClassName(modules, classCouplingReport.clazz)
            for (module in reportRelatedModules) {
                if (classCouplingReportMap.containsKey(module)) {
                    classCouplingReportMap[module]?.add(classCouplingReport)
                    continue
                }
                classCouplingReportMap[module] = mutableListOf(classCouplingReport)
            }
        }
        log.info("Group class to module done.")
        return classCouplingReportMap
    }

    private fun getClassCouplingReports(dependency: List<CallerCalleeCouple>,
                                        modules: List<LogicModule>): List<ClassCouplingReport> =
            dependency.flatMap { listOf(it.callee, it.caller) }.distinct()
                    .map { getClassCouplingReport(it, dependency, modules) }

    fun getClassCouplingReport(clazz: String,
                               dependency: List<CallerCalleeCouple>,
                               modules: List<LogicModule>): ClassCouplingReport {
        val innerFanIn = dependency.filter { it.callee == clazz }.filter { isInSameModule(modules, it) }.count()
        val innerFanOut = dependency.filter { it.caller == clazz }.filter { isInSameModule(modules, it) }.count()
        val outerFanIn = dependency.filter { it.callee == clazz }.filter { !isInSameModule(modules, it) }.count()
        val outerFanOut = dependency.filter { it.caller == clazz }.filter { !isInSameModule(modules, it) }.count()
        return ClassCouplingReport(clazz, innerFanIn, innerFanOut, outerFanIn, outerFanOut)
    }

    private fun isInSameModule(modules: List<LogicModule>, it: CallerCalleeCouple): Boolean {
        val callerModules = getModuleByClassName(modules, it.caller)
        val calleeModules = getModuleByClassName(modules, it.callee)
        return callerModules.intersect(calleeModules).isEmpty()
    }

}

data class ModuleCouplingReport(val module: String,
                                val fanIn: Int,
                                val fanOut: Int) {
    val moduleInstability: Double = if (fanIn + fanOut == 0) 0.0 else fanOut.toDouble() / (fanOut + fanIn)
    val moduleCoupling: Double = if (fanIn + fanOut == 0) 0.0 else 1 - 1.0 / (fanOut + fanIn)

    constructor() : this("", 0, 0)
}

data class NewModuleCouplingReport(val module: String,
                                   @JsonIgnore val classCouplingReports: List<ClassCouplingReport>) {
    val outerModuleInstabilityAverage: Double = classCouplingReports.map { it.outerClassInstability }.average()
    val outerModuleInstabilityMedian: Double = classCouplingReports.map { it.outerClassInstability }.median()
    val outerModuleCouplingAverage: Double = classCouplingReports.map { it.outerClassCoupling }.average()
    val outerModuleCouplingMedian: Double = classCouplingReports.map { it.outerClassCoupling }.median()
    val innerModuleInstabilityAverage: Double = classCouplingReports.map { it.innerClassInstability }.average()
    val innerModuleInstabilityMedian: Double = classCouplingReports.map { it.innerClassInstability }.median()
    val innerModuleCouplingAverage: Double = classCouplingReports.map { it.innerClassCoupling }.average()
    val innerModuleCouplingMedian: Double = classCouplingReports.map { it.innerClassCoupling }.median()

}

data class ClassCouplingReport(val clazz: String,
                               val innerFanIn: Int,
                               val innerFanOut: Int,
                               val outerFanIn: Int,
                               val outerFanOut: Int) {
    val innerClassInstability: Double = if (innerFanIn + innerFanOut == 0) 0.0 else innerFanOut.toDouble() / (innerFanOut + innerFanIn)
    val innerClassCoupling: Double = if (innerFanIn + innerFanOut == 0) 0.0 else 1 - 1.0 / (innerFanOut + innerFanIn)
    val outerClassInstability: Double = if (outerFanIn + outerFanOut == 0) 0.0 else outerFanOut.toDouble() / (outerFanOut + outerFanIn)
    val outerClassCoupling: Double = if (outerFanIn + outerFanOut == 0) 0.0 else 1 - 1.0 / (outerFanOut + outerFanIn)
}
