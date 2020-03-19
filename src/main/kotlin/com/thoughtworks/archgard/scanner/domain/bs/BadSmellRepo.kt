package com.thoughtworks.archgard.scanner.domain.bs

interface BadSmellRepo {
    fun save(badSmell: List<BadSmell>)
}
