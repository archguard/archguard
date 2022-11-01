package com.thoughtworks.archguard.architecture.domain.model.scan

import java.time.Instant

class ScanInfo private constructor(val id: String, val archSystemId: String) {
    // todo ValueObject repo
    var repoType: RepoType? = null
    var repo: String? = null
    var branch: String? = null

    var language: Language? = null

    var scanStatus: ScanStatus? = null
    var startTime: Instant? = null
    var endTime: Instant? = null

    enum class RepoType {
        GIT, LOCAL
    }

    enum class Language {
        JAVA, KOTLIN
    }

    enum class ScanStatus {
        SCANNING, SCANNED, FAILED
    }
}

