package com.thoughtworks.archguard.smartscanner.repository

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

object RepositoryHelper {
    fun generateId(): String = UUID.randomUUID().toString()
    fun getCurrentTime(): String =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}
