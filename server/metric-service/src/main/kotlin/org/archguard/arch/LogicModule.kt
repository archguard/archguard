package org.archguard.arch

import org.archguard.graph.Node
import org.slf4j.LoggerFactory

/**
 * The `LogicModule` class represents a logical module in the Kotlin language. It is an entity that must have an ID.
 *
 * This class extends the `LogicComponent` class and implements the `Node` interface.
 *
 * @property id The ID of the logic module.
 * @property name The name of the logic module.
 * @property log A logger instance for logging purposes.
 * @property type The type of the logic module.
 * @property members The list of logic components that are part of this logic module.
 * @property logicMembers The list of logic components that are part of the logic module's logic members.
 * @property status The status of the logic module.
 *
 * @constructor Creates a `LogicModule` instance with the given ID and name.
 * @param id The ID of the logic module.
 * @param name The name of the logic module.
 *
 * @constructor Creates a `LogicModule` instance with the given ID, name, and members.
 * @param id The ID of the logic module.
 * @param name The name of the logic module.
 * @param members The list of logic components that are part of this logic module.
 *
 * @constructor Creates a `LogicModule` instance with the given ID, name, members, and logic members.
 * @param id The ID of the logic module.
 * @param name The name of the logic module.
 * @param members The list of logic components that are part of this logic module.
 * @param logicMembers The list of logic components that are part of the logic module's logic members.
 *
 * @property createWithOnlyLeafMembers A factory method that creates a `LogicModule` instance with only leaf members.
 * @param id The ID of the logic module.
 * @param name The name of the logic module.
 * @param leafMembers The list of leaf logic components that are part of this logic module.
 * @return The created `LogicModule` instance.
 *
 * @property create A factory method that creates a `LogicModule` instance with leaf members and logic members.
 * @param id The ID of the logic module.
 * @param name The name of the logic module.
 * @param leafMembers The list of leaf logic components that are part of this logic module.
 * @param logicMembers The list of logic components that are part of the logic module's logic members.
 * @return The created `LogicModule` instance.
 *
 * @method hide Sets the status of the logic module to "HIDE".
 *
 * @method show Sets the status of the logic module to "NORMAL".
 *
 * @method reverse Reverses the status of the logic module. If the status is "NORMAL", it sets it to "HIDE", and vice versa.
 * @throws RuntimeException if the logic module status is illegal.
 *
 * @method isHideStatus Checks if the logic module status is "HIDE".
 * @return `true` if the logic module status is "HIDE", `false` otherwise.
 *
 * @method isNormalStatus Checks if the logic module status is "NORMAL".
 * @return `true` if the logic module status is "NORMAL", `false` otherwise.
 *
 * @method add Adds a logic component to the logic module.
 * @param logicComponent The logic component to be added.
 *
 * @method remove Removes a logic component from the logic module.
 * @param logicComponent The logic component to be removed.
 *
 * @method getSubLogicComponent Returns the list of logic components that are part of the logic module.
 * @return The list of logic components.
 *
 * @method getSubJClassComponent Returns the list of logic components that are JClass components (ModuleMemberType.CLASS).
 * @return The list of JClass components.
 *
 * @method getSubSubModuleComponent Returns the list of logic components that are submodule components (ModuleMemberType.SUBMODULE).
 * @return The list of submodule components.
 *
 * @method getSubLogicModuleComponent Returns the list of logic components that are logic module components (ModuleMemberType.LOGIC_MODULE).
 * @return The list of logic module components.
 *
 * @method isService Checks if the logic module is a service. A logic module is considered a service if it does not have any JClass components, submodule components, and has at least one logic module component.
 * @return `true` if the logic module is a service, `false` otherwise.
 *
 * @method isLogicModule Checks if the logic module is a logic module. A logic module is considered a logic module if it has at least one JClass component or submodule component, and does not have any logic module components.
 * @return `true` if the logic module is a logic module, `false` otherwise.
 *
 * @method isMixture Checks if the logic module is a mixture. A logic module is considered a mixture if it has at least one JClass component or submodule component, and has at least one logic module component.
 * @return `true` if the logic module is a mixture, `false` otherwise.
 *
 * @method fixType Fixes the type of the logic module based on its composition of logic components.
 *
 * @method containsOrEquals Checks if the logic module contains or equals the given logic component.
 * @param logicComponent The logic component to check.
 * @return `true` if the logic module contains or equals the logic component, `false` otherwise.
 *
 * @method getFullName Returns the full name of the logic module.
 * @return The full name of the logic module.
 *
 * @method getType Returns the type of the logic module (ModuleMemberType.LOGIC_MODULE).
 * @return The type of the logic module.
 *
 * @method getNodeId Returns the ID of the logic module.
 * @return The ID of the logic module.
 */
data class LogicModule private constructor(val id: String, val name: String) : LogicComponent(), Node {
    private val log = LoggerFactory.getLogger(LogicModule::class.java)
    private var type: LogicModuleType? = null

    @Deprecated("Please use Factory Method")
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
        return members.filter { it.getType() == LogicModuleMemberType.CLASS }
    }

    private fun getSubSubModuleComponent(): List<LogicComponent> {
        return members.filter { it.getType() == LogicModuleMemberType.SUBMODULE }
    }

    private fun getSubLogicModuleComponent(): List<LogicComponent> {
        return members.filter { it.getType() == LogicModuleMemberType.LOGIC_MODULE }
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

    override fun getType(): LogicModuleMemberType {
        return LogicModuleMemberType.LOGIC_MODULE
    }

    override fun getNodeId(): String {
        return id
    }
}
