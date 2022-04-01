package com.thoughtworks.archguard.datamap.controller.domain

interface DatamapRepository {
    fun getDatamapBySystemId(systemId: Long): List<Datamap>
}