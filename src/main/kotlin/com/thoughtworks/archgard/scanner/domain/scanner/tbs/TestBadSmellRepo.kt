package com.thoughtworks.archgard.scanner.domain.scanner.tbs

interface TestBadSmellRepo {
    fun save(testBadSmells: List<TestBadSmell>)
    fun saveCount(count: Int)
}