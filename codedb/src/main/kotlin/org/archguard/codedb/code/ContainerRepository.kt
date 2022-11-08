package org.archguard.codedb.code

import org.archguard.codedb.domain.ContainerServiceModel
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono


interface ContainerRepository : ReactiveMongoRepository<ContainerServiceModel, String> {
    fun deleteAllBySystemId(systemId: String): Mono<Void>
}