package com.thoughtworks.archguard.config.domain

interface ConfigureRepository {
    fun getConfigures(): List<NodeConfigure>
    fun create(config: NodeConfigure)
    fun update(config: NodeConfigure)
    fun delete(id: String)
    fun getConfiguresByType(type: String): List<NodeConfigure>
}
