package com.thoughtworks.archgard.scanner.domain.scanner.bak.bs

interface BadSmellRepo {
    fun save(badSmell: List<BadSmell>)
}
