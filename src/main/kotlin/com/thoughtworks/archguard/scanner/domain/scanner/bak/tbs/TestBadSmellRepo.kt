package com.thoughtworks.archguard.scanner.domain.scanner.bak.tbs

interface TestBadSmellRepo {
    fun save(testBadSmells: List<TestBadSmell>)
    fun saveCount(count: Int)
}