package org.archguard.codedb.code

import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


interface CodeRepository : ReactiveMongoRepository<CodeDocument, String> {
    fun deleteAllBySystemId(systemId: String) : Mono<Void>
    fun findBySystemId(systemId: String): Flux<List<CodeDocument>>
    fun findBySystemIdAndLanguage(systemId: String, language: String): Flux<List<CodeDocument>>
    fun findBySystemIdAndLanguageAndPath(systemId: String, language: String, path: String): Flux<List<CodeDocument>>
}