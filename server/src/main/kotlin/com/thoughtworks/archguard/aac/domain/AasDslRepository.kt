package com.thoughtworks.archguard.aac.domain

interface AasDslRepository {
    fun save(model: AacDslCodeModel): Long?
    fun update(model: AacDslCodeModel): Int
    fun getById(id: Long): AacDslCodeModel?
}
