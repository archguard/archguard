package com.thoughtworks.archguard.scanner.infrastructure.db

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Repository
import java.io.File

@Repository
class SqlScriptRunner(val jdbi: Jdbi) {
    fun run(sql: File) {
        jdbi.open().use { h -> h.createScript(sql.readText()).execute() }
    }

    fun run(sqls: List<File>) {
        sqls.forEach { sql -> jdbi.open().use { h -> h.createScript(sql.readText()).execute() } }
    }

    fun run(sql: String) {
        jdbi.open().use { h -> h.createScript(sql).execute() }
    }
}
