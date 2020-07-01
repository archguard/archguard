package com.thoughtworks.archguard.module.domain

class ReferenceConfig(val id: String, val beanId: String, val interfaceName: String, val version: String?,
                      var group: String?, val subModule: SubModule) {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReferenceConfig

        if (id != other.id) return false
        if (beanId != other.beanId) return false
        if (interfaceName != other.interfaceName) return false
        if (version != other.version) return false
        if (group != other.group) return false
        if (subModule != other.subModule) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + beanId.hashCode()
        result = 31 * result + interfaceName.hashCode()
        result = 31 * result + (version?.hashCode() ?: 0)
        result = 31 * result + (group?.hashCode() ?: 0)
        result = 31 * result + subModule.hashCode()
        return result
    }

    override fun toString(): String {
        return "ReferenceConfig(id='$id', beanId='$beanId', interfaceName='$interfaceName', version=$version, group=$group, subModule=$subModule)"
    }
}

enum class MatchType {
    ANY, MULTI, ONE, NONE
}