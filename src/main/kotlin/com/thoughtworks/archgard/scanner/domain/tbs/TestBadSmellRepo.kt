package com.thoughtworks.archgard.scanner.domain.tbs

interface TestBadSmellRepo {
    fun save(testBadSmells: List<TestBadSmell>)
}