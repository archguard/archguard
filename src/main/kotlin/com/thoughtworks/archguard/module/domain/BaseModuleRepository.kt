package com.thoughtworks.archguard.module.domain

interface BaseModuleRepository {
    fun getBaseModules(): List<String>

    fun getJClassesHasModules(): List<JClass>

    fun getJClassesById(id: String): JClass
}
