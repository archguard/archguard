package org.archguard.scanner.common

import java.text.SimpleDateFormat
import java.util.*

object RepositoryHelper {
    fun generateId(): String {
        return UUID.randomUUID().toString()
    }

    val currentTime: String
        get() {
            val dt = Date()
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return sdf.format(dt)
        }
}
