package com.thoughtworks.archguard.project_info.domain

data class ProjectInfoDTO(var id: String = "",
                          val projectName: String = "",
                          val repo: List<String> = ArrayList(),
                          val sql: String = "",
                          val username: String = "",
                          val password: String = "",
                          val repoType: String = "GIT")
