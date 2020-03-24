package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.checkstyle.CheckStyle
import com.thoughtworks.archgard.scanner.domain.scanner.checkstyle.CheckStyleRepo
import org.springframework.stereotype.Repository

@Repository
class CheckStyleRepoImpl : CheckStyleRepo {
    override fun save(checkStyle: List<CheckStyle>) {

    }

}