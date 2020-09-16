package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import java.io.PrintWriter

/*
* core scanner
*/

class ScannerService(private val gitAdapter: JGitAdapter,
                     private val bean2Sql: Bean2Sql) {

    fun git2SqlFile(gitPath: String, branch: String, after: String, repoId: String, systemId: Long) {
        PrintWriter("output.sql").use { out ->
            gitAdapter.scan(gitPath, branch, after, repoId, systemId) { model ->
                out.println(bean2Sql.bean2Sql(model))
            }
        }
    }

}
