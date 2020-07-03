package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.JClass

interface BaseModuleRepository {
    fun getBaseModules(): List<String>

    fun getJClassesHasModules(): List<JClass>

}
