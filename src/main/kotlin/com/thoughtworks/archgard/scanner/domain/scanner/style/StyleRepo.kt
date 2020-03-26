package com.thoughtworks.archgard.scanner.domain.scanner.style

interface StyleRepo {
    fun save(style: List<Style>)
    fun deleteAll()
}
