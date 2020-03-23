package com.thoughtworks.archgard.scanner.domain.project

import com.thoughtworks.archgard.scanner.domain.project.ProjectInfo

interface ProjectRepository {

    fun getProjectInfo(): ProjectInfo
}