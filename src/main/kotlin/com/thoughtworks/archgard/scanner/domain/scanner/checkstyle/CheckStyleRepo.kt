package com.thoughtworks.archgard.scanner.domain.scanner.checkstyle

interface CheckStyleRepo {
    fun save(checkStyle: List<CheckStyle>)
}
