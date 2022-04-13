package com.thoughtworks.archguard.code.module.domain.model

import com.thoughtworks.archguard.code.module.domain.graph.Node
import org.slf4j.LoggerFactory

/**
 * LogicModule is an Entity, so it must have an id.
 */
class LogicModule private constructor(val id: String, val name: String) : LogicComponent(), Node {
    private val log = LoggerFactory.getLogger(LogicModule::class.java)
    private var type: LogicModuleType? = null

//    @Deprecated("Please use Factory Method")
    constructor(id: String, name: String, members: List<LogicComponent>) : this(id, name) {
        this.members = members
        this.type = LogicModuleType.LOGIC_MODULE
    }

    private constructor(id: String, name: String, members: List<LogicComponent>, logicMembers: List<LogicComponent>) : this(id, name, members) {
        this.logicMembers = logicMembers
    }

    companion object {
        fun createWithOnlyLeafMembers(id: String, name: String, leafMembers: List<LogicComponent>): LogicModule {
            return LogicModule(id, name, leafMembers)
        }

        fun create(id: String, name: String, leafMembers: List<LogicComponent>, logicMembers: List<LogicComponent>): LogicModule {
            val logicModule = LogicModule(id, name, leafMembers, logicMembers)
            when {
                logicModule.isService() -> {
                    logicModule.type = LogicModuleType.SERVICE
                }
                logicModule.isLogicModule() -> {
                    logicModule.type = LogicModuleType.LOGIC_MODULE
                }
                else -> {
                    logicModule.type = LogicModuleType.MIXTURE
                }
            }
            return logicModule
        }

    }

    var members: List<LogicComponent> = emptyList()
    private var logicMembers: List<LogicComponent> = emptyList()
    var status = LogicModuleStatus.NORMAL

    fun hide() {
        this.status = LogicModuleStatus.HIDE
    }

    fun show() {
        this.status = LogicModuleStatus.NORMAL
    }

    fun reverse() {
        if (isNormalStatus()) {
            hide()
            return
        }
        if (isHideStatus()) {
            show()
            return
        }
        throw RuntimeException("Illegal logic module status!")
    }

    private fun isHideStatus() = this.status == LogicModuleStatus.HIDE

    private fun isNormalStatus() = this.status == LogicModuleStatus.NORMAL

    override fun add(logicComponent: LogicComponent) {
        if (!containsOrEquals(logicComponent)) {
            val mutableList = members.toMutableList()
            mutableList.add(logicComponent)
            members = mutableList.toList()
            fixType()
            return
        }
        log.error("{} already exists in this container", logicComponent)
    }

    override fun remove(logicComponent: LogicComponent) {
        if (!members.contains(logicComponent)) {
            val mutableList = members.toMutableList()
            mutableList.remove(logicComponent)
            members = mutableList.toList()
            fixType()
            return
        }
        log.error("{} not exists in this container's members", logicComponent)
    }

    override fun getSubLogicComponent(): List<LogicComponent> {
        return members
    }

    private fun getSubJClassComponent(): List<LogicComponent> {
        return members.filter { it.getType() == ModuleMemberType.CLASS }
    }

    private fun getSubSubModuleComponent(): List<LogicComponent> {
        return members.filter { it.getType() == ModuleMemberType.SUBMODULE }
    }

    private fun getSubLogicModuleComponent(): List<LogicComponent> {
        return members.filter { it.getType() == ModuleMemberType.LOGIC_MODULE }
    }

    fun isService(): Boolean {
        return getSubJClassComponent().isEmpty() && getSubSubModuleComponent().isEmpty() && getSubLogicModuleComponent().isNotEmpty()
    }

    fun isLogicModule(): Boolean {
        return (getSubJClassComponent().isNotEmpty() || getSubSubModuleComponent().isNotEmpty()) && getSubLogicModuleComponent().isEmpty()
    }

    private fun isMixture(): Boolean {
        return (getSubJClassComponent().isNotEmpty() || getSubSubModuleComponent().isNotEmpty()) && getSubLogicModuleComponent().isNotEmpty()
    }

    private fun fixType() {
        when {
            isService() -> {
                this.type = LogicModuleType.SERVICE
            }
            isLogicModule() -> {
                this.type = LogicModuleType.LOGIC_MODULE
            }
            isMixture() -> {
                this.type = LogicModuleType.MIXTURE
            }
        }
    }

    override fun containsOrEquals(logicComponent: LogicComponent): Boolean {
        if (logicComponent in members) {
            return true
        }
        if (logicComponent in logicMembers) {
            return true
        }
        return members.map { it.containsOrEquals(logicComponent) }.contains(true) || logicMembers.map { it.containsOrEquals(logicComponent) }.contains(true)
    }

    override fun getFullName(): String {
        return name
    }

    override fun getType(): ModuleMemberType {
        return ModuleMemberType.LOGIC_MODULE
    }

    override fun getNodeId(): String {
        return id
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LogicModule

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "LogicModule(id='$id', name='$name', members=$members, logicMembers=$logicMembers, status=$status)"
    }
}

enum class LogicModuleStatus {
    NORMAL, HIDE
}

enum class LogicModuleType {
    SERVICE, LOGIC_MODULE, MIXTURE
}
