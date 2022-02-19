package com.thoughtworks.archguard.scanner.domain.scanner.bak.bs

interface BadSmellRepo {
    fun save(badSmell: List<BadSmell>)
}
