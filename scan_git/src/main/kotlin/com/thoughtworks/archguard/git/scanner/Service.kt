package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import java.io.File

/*
* core scanner
*/

class ScannerService(private val gitAdapter: JGitAdapter,
                     private val bean2Sql: Bean2Sql) {

    fun git2SqlFile(gitPath: String, branch: String, after: String, repoId: String, systemId: Long) {
        val result = gitAdapter.scan(gitPath, branch, after, repoId, systemId)
        val file = File("output.sql")
        if (file.exists()) {
            file.delete()
        }
        file.appendText(result.first.joinToString("\n") { bean2Sql.bean2Sql(it) })
        file.appendText(result.second.joinToString("\n") { bean2Sql.bean2Sql(it) })
    }

}
