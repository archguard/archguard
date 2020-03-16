package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import java.io.PrintWriter

/*
* core scanner
*/

class ScannerService(private val gitAdapter: GitAdapter,
                     private val bean2Sql: Bean2Sql) {

    fun git2SqlFile(config: Config) {
        PrintWriter("output.sql").use { out ->
            gitAdapter.scan(config) { model ->
                out.println(bean2Sql.bean2Sql(model))
            }
        }
    }

}
