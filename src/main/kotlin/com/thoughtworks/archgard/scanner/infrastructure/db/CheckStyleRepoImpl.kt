package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.checkstyle.CheckStyle
import com.thoughtworks.archgard.scanner.domain.scanner.checkstyle.CheckStyleRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class CheckStyleRepoImpl(@Autowired private val checkStylesDao: CheckStyleDao) : CheckStyleRepo {

    override fun save(checkStyle: List<CheckStyle>) {
        checkStylesDao.saveAll(checkStyle)
    }

    override fun deleteAll() {
        checkStylesDao.deleteAll()
    }

}