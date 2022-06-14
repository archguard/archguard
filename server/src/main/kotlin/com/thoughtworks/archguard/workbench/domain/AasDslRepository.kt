package com.thoughtworks.archguard.workbench.domain

interface AasDslRepository {
    fun save(model: AacDslCodeModel): Long?
    fun update(model: AacDslCodeModel): Int
    fun getById(id: Long): AacDslCodeModel?
}
