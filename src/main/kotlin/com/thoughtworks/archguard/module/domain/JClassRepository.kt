package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.JClass

interface JClassRepository {
    fun getJClassBy(name: String, module: String): JClass?

    fun getJClassById(id: String): JClass?

    fun getAll(): List<JClass>

    fun getJClassesHasModules(): List<JClass>

}