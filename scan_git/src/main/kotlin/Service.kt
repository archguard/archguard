package com.thoughtworks.archguard.git.scanner

import java.io.PrintWriter
import kotlin.reflect.jvm.kotlinProperty

/*
* core scanner
*/

class ScannerService(private val gitAdapter: GitAdapter) {

    fun git2SqlFile(config: Config) {
        PrintWriter("output.sql").use { out ->
            gitAdapter.scan(config) { model ->
                val columns = arrayListOf<String>()
                val values = arrayListOf<Any>()
                val clazz = model.javaClass
                clazz.declaredFields.iterator().forEach {
                    val property = it.kotlinProperty
                    columns.add(property!!.name)
                    values.add(property.call(model)!!)
                }
                val valueString = values.joinToString {
                    if (it is String) "'$it'" else it.toString()
                }
                val sql = "insert into ${clazz.simpleName}(${columns.joinToString()}) values($valueString);"
                out.println(sql)
            }
        }
    }

}
