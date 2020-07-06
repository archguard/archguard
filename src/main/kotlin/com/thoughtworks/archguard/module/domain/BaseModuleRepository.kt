package com.thoughtworks.archguard.module.domain

interface BaseModuleRepository {
    fun getBaseModules(): List<String>

}
