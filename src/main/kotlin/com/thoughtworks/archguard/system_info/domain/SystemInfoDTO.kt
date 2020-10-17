package com.thoughtworks.archguard.system_info.domain

import java.sql.Timestamp

data class SystemInfoDTO(var id: Long? = null,
                         val systemName: String = "",
                         val repo: List<String> = ArrayList(),
                         val sql: String = "",
                         val username: String = "",
                         val password: String = "",
                         val scanned: ScannedType = ScannedType.NONE,
                         val qualityGateProfileId: Long? = null,
                         val repoType: String = "GIT",
                         val updatedTime: Timestamp? = null)
