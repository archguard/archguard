package com.thoughtworks.archguard.git.scanner.loc

import com.thoughtworks.archguard.git.scanner.loc.model.JMethodLoC
import java.io.File

class JavaStoreImpl {
    private val buffer: MutableList<String> = ArrayList()
    fun saveClassLoC(clz: String?, loc: Int, systemId: String, module: String, methodLocs: ArrayList<JMethodLoC>) {
        val classSql = String.format("UPDATE JClass SET loc = %d WHERE name = '%s' and system_id='%s' and module='%s';",
                loc, clz, systemId, module)
        buffer.add(classSql)

        val methodSql = methodLocs.map { "UPDATE JMethod SET loc = ${it.loc} WHERE " +
                "clzname = '$clz' and system_id='$systemId' and module='$module' and name = '${it.methodName}';" }.joinToString("\n")
        buffer.add(methodSql)
    }

    fun toFile() {
        val file = File("loc_output.sql")
        if (file.exists()) {
            file.delete()
        }
        file.appendText(buffer.joinToString("\n"))
    }
}