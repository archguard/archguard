package com.thoughtworks.archgard.scanner.domain.project

interface ProjectRepository {

    fun getProjectInfo(id: Long): ProjectInfo?
}
