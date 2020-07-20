package com.thoughtworks.archguard.project_info.domain


interface ProjectInfoRepository {
    fun getProjectInfo(): ProjectInfo?

    fun updateProjectInfo(projectInfo: ProjectInfo): Int

    fun addProjectInfo(projectInfo: ProjectInfo): String

    fun querySizeOfProjectInfo(): Int

}
