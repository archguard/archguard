package com.thoughtworks.archgard.scanner.domain.scanner.bs

interface BadSmellRepo {
    fun save(badSmell: List<BadSmell>)
}
