package org.archguard.scanner.analyser.api.postman

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream

class PostmanReader {
    var om: ObjectMapper = ObjectMapper()

    init {
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    @Throws(JsonParseException::class, JsonMappingException::class, IOException::class)
    fun readCollectionFileClasspath(fileOnClasspath: String): PostmanCollection {
        val fileName = fileOnClasspath.substring(fileOnClasspath.indexOf(":") + 1)
        val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
        val collection = om.readValue(stream, PostmanCollection::class.java)
        stream.close()
        return collection
    }

    @Throws(JsonParseException::class, JsonMappingException::class, IOException::class)
    fun readEnvironmentFileClasspath(fileOnClasspath: String): PostmanEnvironment {
        val fileName = fileOnClasspath.substring(fileOnClasspath.indexOf(":") + 1)
        val stream = Thread.currentThread().contextClassLoader.getResourceAsStream(fileName)
        val env = om.readValue(stream, PostmanEnvironment::class.java)
        stream.close()
        return env
    }

    @Throws(IOException::class)
    fun readCollectionFile(filePath: String): PostmanCollection {
        if (filePath.startsWith("classpath:")) {
            return readCollectionFileClasspath(filePath)
        }
        val stream: InputStream = FileInputStream(File(filePath))
        val collection = om.readValue(stream, PostmanCollection::class.java)
        stream.close()
        return collection
    }

    @Throws(IOException::class)
    fun readEnvironmentFile(filePath: String?): PostmanEnvironment {
        if (filePath == null) {
            return PostmanEnvironment()
        }
        if (filePath.startsWith("classpath:")) {
            return readEnvironmentFileClasspath(filePath)
        }
        val stream: InputStream = FileInputStream(File(filePath))
        val env = om.readValue(stream, PostmanEnvironment::class.java)
        stream.close()
        return env
    }
}
