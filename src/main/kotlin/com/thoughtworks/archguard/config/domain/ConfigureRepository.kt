package com.thoughtworks.archguard.config.domain

interface ConfigureRepository {
    fun getConfigures(): List<Configure>
    fun create(config: Configure)
    fun update(config: Configure)
    fun delete(id: String)
    fun getConfiguresByType(type: String): List<Configure>
    fun deleteConfiguresByType(type: String)
    fun batchCreateConfigures(configs: List<Configure>)
}
