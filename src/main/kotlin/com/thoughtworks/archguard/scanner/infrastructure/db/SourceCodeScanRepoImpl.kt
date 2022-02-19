package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.codescan.SourceCodeScanRepo
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class SourceCodeScanRepoImpl(@Autowired val sqlScriptRunner: SqlScriptRunner, @Autowired val jdbi: Jdbi) : SourceCodeScanRepo {

    private val log = LoggerFactory.getLogger(SourceCodeScanRepoImpl::class.java)

    override fun updateJClassLoc(sql: File) {
        log.info("saving loc report to DB")
        sqlScriptRunner.run(sql)
    }
}