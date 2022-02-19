package com.thoughtworks.archguard.scanner.infrastructure.db

import com.thoughtworks.archguard.scanner.domain.scanner.bak.style.Style
import com.thoughtworks.archguard.scanner.domain.scanner.bak.style.StyleRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class StyleRepoImpl(@Autowired private val stylesDao: StyleDao) : StyleRepo {

    override fun save(style: List<Style>) {
        stylesDao.saveAll(style)
    }

    override fun deleteAll() {
        stylesDao.deleteAll()
    }

}