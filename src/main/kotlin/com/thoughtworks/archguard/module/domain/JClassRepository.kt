package com.thoughtworks.archguard.module.domain

import com.thoughtworks.archguard.module.domain.model.Dependency
import com.thoughtworks.archguard.module.domain.model.JClass
import com.thoughtworks.archguard.module.domain.model.ModuleMember

interface JClassRepository {
    fun getJClassBy(name: String, module: String): JClass?

    fun getJClassById(id: String): JClass?

    fun getAll(): List<JClass>

    fun getJClassesHasModules(): List<JClass>

    fun getAllClassDependency(members: List<ModuleMember>): List<Dependency<JClass>>

}