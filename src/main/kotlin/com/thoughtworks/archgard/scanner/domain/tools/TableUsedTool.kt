package com.thoughtworks.archgard.scanner.domain.tools

import com.thoughtworks.archgard.scanner.infrastructure.Processor
import java.io.File

class TableUsedTool(val projectRoot: File, val sql: String?) {

    fun analyse() {
        if (sql != null) {
            getTableName(sql)
        }
    }

    private fun getTableName(sql: String) {
        val sqlFile = File(projectRoot.toString() + "/sql_tables.sql")
        sqlFile.writeText(sql, charset("UTF-8"))
        val outputFile = File(projectRoot.toString() + "table_names.log")
        call(listOf("/bin/sh", "-c",
                "grep \"CREATE TABLE\" $sqlFile " +
                        "|awk '{print \$3}'" +
                        "|awk -F '`' '{print \$2}' > $outputFile"))
    }

    private fun call(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), projectRoot)
    }
}