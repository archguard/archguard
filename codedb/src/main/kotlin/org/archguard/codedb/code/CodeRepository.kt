package org.archguard.codedb.code

import org.archguard.codedb.code.domain.CodeDocument
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono


interface CodeRepository : ReactiveMongoRepository<CodeDocument, String> {
    fun deleteAllBySystemId(systemId: String): Mono<Void>
}