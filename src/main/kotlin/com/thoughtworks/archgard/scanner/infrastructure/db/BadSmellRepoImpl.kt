package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.bak.bs.BadSmell
import com.thoughtworks.archgard.scanner.domain.scanner.bak.bs.BadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class BadSmellRepoImpl(@Autowired private val badSmellModelDao: BadSmellDao) : BadSmellRepo {

    override fun save(badSmell: List<BadSmell>) {
        badSmellModelDao.saveAll(badSmell)
    }
}
