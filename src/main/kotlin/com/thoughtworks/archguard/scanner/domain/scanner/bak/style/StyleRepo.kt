package com.thoughtworks.archguard.scanner.domain.scanner.bak.style

interface StyleRepo {
    fun save(style: List<Style>)
    fun deleteAll()
}
