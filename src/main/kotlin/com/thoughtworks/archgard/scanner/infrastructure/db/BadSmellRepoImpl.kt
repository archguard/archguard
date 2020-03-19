package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.bs.BadSmell
import com.thoughtworks.archgard.scanner.domain.bs.BadSmellRepo
import org.springframework.stereotype.Repository

@Repository
class BadSmellRepoImpl : BadSmellRepo {
    override fun save(badSmell: List<BadSmell>) {
        TODO("Not yet implemented")
    }
}