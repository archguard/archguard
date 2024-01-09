package org.archguard.protocol.dubbo

/**
 * The `ReferenceConfig` class represents the configuration for a reference to a bean in the Kotlin language.
 * It contains information such as the ID, bean ID, interface name, version, group, and sub-module.
 *
 * @property id The ID of the reference.
 * @property beanId The bean ID of the reference.
 * @property interfaceName The name of the interface for the reference.
 * @property version The version of the reference. Can be null.
 * @property group The group of the reference. Can be null.
 * @property subModule The sub-module of the reference.
 *
 * @constructor Creates a new `ReferenceConfig` instance with the specified properties.
 *
 * @param id The ID of the reference.
 * @param beanId The bean ID of the reference.
 * @param interfaceName The name of the interface for the reference.
 * @param version The version of the reference. Can be null.
 * @param group The group of the reference. Can be null.
 * @param subModule The sub-module of the reference.
 *
 * @throws RuntimeException if the version or group is "*", or if they are null when they should not be.
 */
data class ReferenceConfig(
    val id: String,
    val beanId: String,
    val interfaceName: String,
    val version: String?,
    var group: String?,
    val subModule: SubModuleDubbo
) {
    private val versionType: MatchType = matchType(version)
    private val groupType: MatchType = matchType(group)

    fun getVersions(): List<String> {
        if (hasSpecificVersions()) {
            return this.version!!.split(",").map { it.trim() }
        }
        throw RuntimeException("Should not be called when version is * or null")
    }

    fun hasSpecificVersions() = this.versionType == MatchType.MULTI || this.versionType == MatchType.ONE

    fun getGroups(): List<String> {
        if (hasSpecificGroups()) {
            return this.group!!.split(",").map { it.trim() }
        }
        throw RuntimeException("Should not be called when group is * or null")
    }

    fun hasSpecificGroups() = this.groupType == MatchType.MULTI || this.groupType == MatchType.ONE

    private fun matchType(value: String?): MatchType {
        return when {
            value == null -> MatchType.NONE
            "*" == value -> MatchType.ANY
            value.split(",").size > 1 -> MatchType.MULTI
            value.split(",").size == 1 -> MatchType.ONE
            else -> throw RuntimeException("${value}Match No Type!")
        }
    }

    enum class MatchType {
        ANY, MULTI, ONE, NONE
    }
}
