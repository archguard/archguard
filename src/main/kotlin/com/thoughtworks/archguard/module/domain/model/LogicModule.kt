package com.thoughtworks.archguard.module.domain.model

import org.slf4j.LoggerFactory

/**
 * LogicModule is an Entity, so it must have an id.
 */
class LogicModule(val id: String, val name: String) : LogicComponent() {
    private val log = LoggerFactory.getLogger(LogicModule::class.java)

    constructor(id: String, name: String, members: List<LogicComponent>) : this(id, name) {
        this.members = members
    }

    constructor(id: String, name: String, members: List<LogicComponent>, lgMembers: List<LogicComponent>) : this(id, name, members) {
        this.lgMembers = lgMembers
    }

    companion object {
        fun createWithOnlyLeafMembers(id: String, name: String, leafMembers: List<LogicComponent>): LogicModule {
            return LogicModule(id, name, leafMembers)
        }

        fun createWithOnlyLogicModuleMembers(id: String, name: String, lgMembers: List<LogicComponent>): LogicModule {
            return LogicModule(id, name, emptyList(), lgMembers)
        }

        fun create(id: String, name: String, leafMembers: List<LogicComponent>, lgMembers: List<LogicComponent>): LogicModule {
            return LogicModule(id, name, leafMembers, lgMembers)
        }

    }

    var members: List<LogicComponent> = emptyList()
    var lgMembers: List<LogicComponent> = emptyList()
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
            return
        }
        log.error("{} already exists in this container", logicComponent)
    }

    override fun remove(logicComponent: LogicComponent) {
        if (!members.contains(logicComponent)) {
            val mutableList = members.toMutableList()
            mutableList.remove(logicComponent)
            members = mutableList.toList()
            return
        }
        log.error("{} not exists in this container's members", logicComponent)
    }

    override fun getSubLogicComponent(): List<LogicComponent> {
        return members
    }

    fun getSubJClassComponent(): List<LogicComponent> {
        return members.filter { it.getType() == ModuleMemberType.CLASS }
    }

    fun getSubSubModuleComponent(): List<LogicComponent> {
        return members.filter { it.getType() == ModuleMemberType.SUBMODULE }
    }

    fun getSubLogicModuleComponent(): List<LogicComponent> {
        return members.filter { it.getType() == ModuleMemberType.LOGIC_MODULE }
    }

    override fun containsOrEquals(logicComponent: LogicComponent): Boolean {
        if (logicComponent in members) {
            return true
        }
        return members.map { it.containsOrEquals(logicComponent) }.contains(true)
    }

    override fun getFullName(): String {
        return name
    }

    override fun getType(): ModuleMemberType {
        return ModuleMemberType.LOGIC_MODULE
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
        return "LogicModule(id='$id', name='$name', members=$members, lgMembers=$lgMembers, status=$status)"
    }
}

enum class LogicModuleStatus {
    NORMAL, HIDE
}