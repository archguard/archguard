package com.thoughtworks.archguard.system_info.domain

import java.sql.Timestamp

data class SystemInfo(var id: Long? = null,
                      val systemName: String = "",
                      val repo: String = "",
                      val sql: String = "",
                      val username: String = "",
                      val password: String = "",
                      val scanned: ScannedType = ScannedType.NONE,
                      val qualityGateProfileId: Long? = null,
                      val repoType: String = "GIT",
                      val updatedTime: Timestamp? = null,
                      val badSmellThresholdSuiteId: Long? = null,
                      val branch: String? = "master",
                      val language: String? = "jvm",
                      val codePath: String? = ""
)
