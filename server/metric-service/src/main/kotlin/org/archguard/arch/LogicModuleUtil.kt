package org.archguard.arch

object LogicModuleUtil {
    fun getModule(modules: List<LogicModule>, logicComponent: LogicComponent): List<LogicModule> {
        val callerByFullMatch = fullMatch(logicComponent, modules)
        if (callerByFullMatch.isNotEmpty()) {
            return callerByFullMatch
        }

        return startsWithMatch(logicComponent, modules)
    }


    private fun fullMatch(jClass: LogicComponent, modules: List<LogicModule>): List<LogicModule> {
        return modules.filter { logicModule ->
            logicModule.members.any { moduleMember -> jClass.getFullName() == moduleMember.getFullName() }
        }
    }

    private fun startsWithMatch(jClass: LogicComponent, modules: List<LogicModule>): List<LogicModule> {
        var maxMatchSize = 0
        val matchModule: MutableList<LogicModule> = mutableListOf()
        for (logicModule in modules) {
            val maxMatchSizeInLogicModule = logicModule.members
                .filter { member -> jClass.getFullName().startsWith("${member.getFullName()}.") }
                .maxByOrNull { it.getFullName().length }
                ?: continue

            if (maxMatchSizeInLogicModule.getFullName().length > maxMatchSize) {
                maxMatchSize = maxMatchSizeInLogicModule.getFullName().length
                matchModule.clear()
                matchModule.add(logicModule)
            } else if (maxMatchSizeInLogicModule.getFullName().length == maxMatchSize) {
                matchModule.add(logicModule)
            }
        }

        return matchModule.toList()
    }
}
