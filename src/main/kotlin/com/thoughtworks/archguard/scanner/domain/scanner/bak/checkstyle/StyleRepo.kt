package com.thoughtworks.archguard.scanner.domain.scanner.bak.checkstyle

interface StyleRepo {
    fun save(style: List<Style>)
    fun deleteAll()
}
