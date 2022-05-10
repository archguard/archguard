package com.thoughtworks.archguard.architecture.domain.repository

interface ArchSystemRepository {
    fun getArchSystem(id: String): ArchSystemPO

    fun createArchSystem(archSystemPO: ArchSystemPO): String
}
