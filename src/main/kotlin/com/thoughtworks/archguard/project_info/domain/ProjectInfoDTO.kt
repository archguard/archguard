package com.thoughtworks.archguard.project_info.domain

data class ProjectInfoDTO(var id: Long? = null,
                          val projectName: String = "",
                          val repo: List<String> = ArrayList(),
                          val sql: String = "",
                          val username: String = "",
                          val password: String = "",
                          val scanned: ScannedType = ScannedType.NONE,
                          val qualityGateProfileId: Long? = null,
                          val repoType: String = "GIT")
