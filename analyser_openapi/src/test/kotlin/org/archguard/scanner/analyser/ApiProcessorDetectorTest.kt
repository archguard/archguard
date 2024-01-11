package org.archguard.scanner.analyser;

import org.archguard.scanner.analyser.api.base.ApiProcessor
import org.archguard.scanner.analyser.api.openapi.OpenApiV3Processor
import org.archguard.scanner.analyser.api.parser.PostmanProcessor
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.File

class ApiProcessorDetectorTest {

    @Test
    fun shouldReturnPostmanProcessorWhenWithPostmanIsTrueAndFileExtensionIsJson() {
        // given
        val file = File(javaClass.getResource("/openapi/CircleCI.postman_collection.json")!!.toURI())

        // when
        val result = ApiProcessorDetector.detectApiProcessor(file, withPostman = true)

        // then
        assertTrue(result is PostmanProcessor)
    }
}
