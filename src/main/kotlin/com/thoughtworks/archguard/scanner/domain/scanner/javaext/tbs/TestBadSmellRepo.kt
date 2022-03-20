package com.thoughtworks.archguard.scanner.domain.scanner.javaext.tbs

interface TestBadSmellRepo {
    fun save(testBadSmells: List<TestBadSmell>)
    fun saveCount(count: Int)
}