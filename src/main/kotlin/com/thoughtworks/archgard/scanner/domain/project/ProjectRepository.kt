package com.thoughtworks.archgard.scanner.domain.project

interface ProjectRepository {

    fun getProjectInfo(): Project
}