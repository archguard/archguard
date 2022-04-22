package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.codescan.sca.ScaAnalyserRepo
import com.thoughtworks.archguard.scanner.domain.scanner.git.GitSourceScanRepo
import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class ScaAnalyserRepositoryImpl(@Autowired val sqlScriptRunner: SqlScriptRunner) : ScaAnalyserRepo {
    private val log = LoggerFactory.getLogger(ScaAnalyserRepositoryImpl::class.java)

    @Autowired
    lateinit var jdbi: Jdbi
    override fun cleanupSca(systemId: Long) {
        log.info("clean up project_composition_dependencies for system {}", systemId)
        jdbi.withHandle<Any, RuntimeException> { handle: Handle ->
            handle.execute(
                "delete from project_composition_dependencies where system_id = ?", systemId
            )
        }
    }

    override fun saveSca(sql: File) {
        log.info("saving git report to DB")
        sqlScriptRunner.run(sql)
    }
}
