package com.thoughtworks.archguard.project_info.domain


interface ProjectInfoRepository {
    fun getProjectInfo(id: Long): ProjectInfo?

    fun getProjectInfoList(): List<ProjectInfo>

    fun updateProjectInfo(projectInfo: ProjectInfo): Int

    fun addProjectInfo(projectInfo: ProjectInfo): Long

    fun queryByProjectName(projectName: String): Int

    fun deleteProjectInfo(id: Long): Int

}
