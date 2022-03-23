package com.thoughtworks.archguard.report.domain.overview

class SystemOverview(val repoCount: Int,
                     val moduleCount: Long,
                     val lineCount: Long,
                     val contributorCount: Long = 0,
                     val qualityLevel: String = "C") {

}

class SystemLanguage (
    val language: String,
    val lineCount: Long,
)