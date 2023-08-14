package org.archguard.scanner.analyser.api.base

import org.archguard.scanner.analyser.model.ApiCollection

interface ApiDetailRender {
    fun render(apiCollections: List<ApiCollection>): String {
        val apiDetailsByTag = apiCollections.map { renderCollection(it) }.filter {
            it.isNotBlank()
        }
        return apiDetailsByTag.joinToString("\n\n") { it }
    }

    fun renderCollection(collection: ApiCollection): String
}
