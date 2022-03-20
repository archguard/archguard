package com.thoughtworks.archguard.scanner.domain.scanner.javaext.checkstyle

interface StyleRepo {
    fun save(style: List<Style>)
    fun deleteAll()
}
