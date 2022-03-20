package com.thoughtworks.archguard.scanner.domain.scanner.javaext.bs

interface BadSmellRepo {
    fun save(badSmell: List<BadSmell>)
}
