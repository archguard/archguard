package com.thoughtworks.archguard.git.scanner.loc

import java.io.File
import java.util.*

class JavaStoreImpl {
    private val buffer: MutableList<String> = ArrayList()
    fun saveClassLoC(clz: String?, loc: Int, systemId: String, module: String) {
        val sql = String.format("UPDATE JClass SET loc = %d WHERE name = '%s' and system_id='%s' and module='%s'",
                loc, clz, systemId, module)
        buffer.add(sql)
    }

    fun toFile() {
        val file = File("loc_output.sql")
        if (file.exists()) {
            file.delete()
        }
        file.appendText(buffer.joinToString("\n"))
    }
}