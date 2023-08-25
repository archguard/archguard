package org.archguard.scanner.analyser.api.parser

import org.archguard.scanner.analyser.api.base.ApiProcessor
import org.archguard.scanner.analyser.api.postman.PostmanReader
import org.archguard.scanner.core.openapi.ApiCollection
import java.io.File

class PostmanProcessor(private val file: File) : ApiProcessor {
    override fun convertApi(): List<ApiCollection> {
        val postmanReader = PostmanReader()
        val collection = postmanReader.readCollectionFile(file.absolutePath)
        val postmanParser = PostmanParser()

        return postmanParser.parse(collection)?.map { apiCollection ->
            apiCollection.items.map {
                it.renderDisplayText()
            }

            apiCollection.filename = file.name

            apiCollection
        } ?: emptyList()
    }
}
