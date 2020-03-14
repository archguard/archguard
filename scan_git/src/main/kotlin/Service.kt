package com.thoughtworks.archguard.git.scanner

import com.thoughtworks.archguard.git.scanner.helper.Bean2Sql
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.PrintWriter

/*
* core scanner
*/

@Service
class ScannerService(@Autowired private val gitAdapter: GitAdapter,
                     @Autowired private val bean2Sql: Bean2Sql) {

    fun git2SqlFile(config: Config) {
        PrintWriter("output.sql").use { out ->
            gitAdapter.scan(config) { model ->
                out.println(bean2Sql.bean2Sql(model))
            }
        }
    }

}
