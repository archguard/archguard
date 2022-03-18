package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.codescan.bytecode.ByteCodeScanRepo
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class ByteCodeScanRepoImpl(@Autowired val sqlScriptRunner: SqlScriptRunner, @Autowired val jdbi: Jdbi) :
    ByteCodeScanRepo {

    private val log = LoggerFactory.getLogger(ByteCodeScanRepoImpl::class.java)

    override fun updateJClassLoc(sql: File) {
        log.info("saving loc report to DB")
        sqlScriptRunner.run(sql)
    }
}