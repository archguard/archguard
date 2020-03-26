package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.style.Style
import com.thoughtworks.archgard.scanner.domain.scanner.style.StyleRepo
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