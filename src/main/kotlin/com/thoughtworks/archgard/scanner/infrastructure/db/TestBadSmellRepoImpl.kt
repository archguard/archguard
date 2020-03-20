package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.tbs.TestBadSmell
import com.thoughtworks.archgard.scanner.domain.tbs.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class TestBadSmellRepoImpl(@Autowired private val testBadSmellDao: TestBadSmellDao) : TestBadSmellRepo {
    override fun save(testBadSmells: List<TestBadSmell>) {
        testBadSmellDao.saveAll(testBadSmells)
    }
}