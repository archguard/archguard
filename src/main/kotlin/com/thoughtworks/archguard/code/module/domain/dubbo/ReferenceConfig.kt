package com.thoughtworks.archguard.code.module.domain.dubbo

class ReferenceConfig(
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

    override fun toString(): String {
        return "ReferenceConfig(id='$id', beanId='$beanId', interfaceName='$interfaceName', version=$version, group=$group, subModule=$subModule)"
    }
}

enum class MatchType {
    ANY, MULTI, ONE, NONE
}
