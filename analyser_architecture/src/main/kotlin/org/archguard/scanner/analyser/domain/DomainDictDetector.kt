package org.archguard.scanner.analyser.domain

import org.archguard.architecture.view.concept.DomainModel
import org.archguard.scanner.analyser.domain.tokenizer.CodeNamingTokenizer

object DomainDictDetector {
    fun analysis(ds: List<DomainModel>): List<String> {
        val names: MutableList<String> = mutableListOf()
        ds.forEach { dataStruct ->
            names.add(dataStruct.name)
            names.addAll(dataStruct.fields.map { it })
            names.addAll(dataStruct.behaviors.map { it })
        }

        val codeNamingTokenizer = CodeNamingTokenizer()
        return names.flatMap { codeNamingTokenizer.tokenize(it) }
    }
}
