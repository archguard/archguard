package org.archguard.codedb.code

import chapi.domain.core.CodeDataStruct
import org.archguard.codedb.domain.ContainerService
import org.springframework.web.bind.annotation.*
import reactor.core.Disposable
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*
import java.util.stream.Collectors

@RestController
@RequestMapping("/api/scanner/{systemId}/reporting")
class CodeEmitter(val eventRepository: CodeRepository) {
    @PostMapping(value = ["/class-items"])
    fun save(
        @PathVariable systemId: String,
        @RequestParam language: String,
        @RequestParam path: String,
        @RequestBody inputs: List<CodeDataStruct>,
    ): Mono<Void> {
        val collect = inputs.stream().map { input ->
            CodeDocument(
                UUID.randomUUID().toString(),
                systemId,
                language,
                path,
                input,
                input.Package,
                input.NodeName,
                input.FilePath,
            )
        }.collect(Collectors.toList())

        return eventRepository.deleteCodeDocumentBySystemId(systemId)
            .thenMany(eventRepository.saveAll(collect))
            .then()
    }

    @PostMapping("/container-services")
    fun saveContainerServices(
        @PathVariable systemId: String,
        @RequestParam language: String,
        @RequestParam path: String,
        @RequestBody input: List<ContainerService>,
    ) {

    }
}