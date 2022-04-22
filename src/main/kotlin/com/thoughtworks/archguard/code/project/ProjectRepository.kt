package com.thoughtworks.archguard.code.project

interface ProjectRepository {
    fun getProjectDependencies(systemId: Long): List<CompositionDependencyDTO>
}
