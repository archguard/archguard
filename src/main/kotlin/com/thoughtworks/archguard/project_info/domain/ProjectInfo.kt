package com.thoughtworks.archguard.project_info.domain

data class ProjectInfo(var id: Long? = null,
                       val projectName: String = "",
                       val repo: String = "",
                       val sql: String = "",
                       val username: String = "",
                       val password: String = "",
                       val repoType: String = "GIT")
