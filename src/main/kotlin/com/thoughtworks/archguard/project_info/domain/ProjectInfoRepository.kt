package com.thoughtworks.archguard.project_info.domain


interface ProjectInfoRepository {
    fun getProjectInfo(): ProjectInfo?

    fun updateProjectInfo(projectInfo: ProjectInfo): Int

    fun addProjectInfo(projectInfo: ProjectInfo): Long

    fun queryByProjectName(projectName: String): Int

}
