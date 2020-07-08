package com.thoughtworks.archguard.config.domain

interface ConfigureRepository {
    fun getConfigures(): List<NodeConfigure>
}
