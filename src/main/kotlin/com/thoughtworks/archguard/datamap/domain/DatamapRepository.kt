package com.thoughtworks.archguard.datamap.domain

interface DatamapRepository {
    fun getDatamapBySystemId(systemId: Long): List<Datamap>
}