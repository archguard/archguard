package org.archguard.codedb.code

import chapi.domain.core.CodeDataStruct
import org.archguard.codedb.domain.ContainerService
import org.springframework.web.bind.annotation.*
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
    ) {
        inputs.stream()
            .map { input ->
                val id = UUID.randomUUID().toString()
                CodeDocument(id, systemId, language, path, input)
            }
            .collect(Collectors.toList())
            .let { eventRepository.saveAll(it) }
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