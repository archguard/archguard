package com.thoughtworks.archgard.scanner2.domain.repository

import com.thoughtworks.archgard.scanner2.domain.model.JClass
import com.thoughtworks.archgard.scanner2.domain.model.JField

interface JClassRepository {

    fun getJClassesHasModules(systemId: Long): List<JClass>

    fun findClassParents(systemId: Long, module: String?, name: String?): List<JClass>

    fun findClassImplements(systemId: Long, name: String?, module: String?): List<JClass>

    fun findFields(id: String): List<JField>
}
