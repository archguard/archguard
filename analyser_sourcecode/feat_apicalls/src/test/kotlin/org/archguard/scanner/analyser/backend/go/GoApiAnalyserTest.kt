package org.archguard.scanner.analyser.backend.go

import chapi.domain.core.CodeDataStruct
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

internal class GoApiAnalyserTest {
    private fun loadNodes(source: String): List<CodeDataStruct> {
        return Json { ignoreUnknownKeys = true }.decodeFromString(
            File(this.javaClass.classLoader.getResource(source)!!.file).readText()
        )
    }

    @Test
    fun analysisByNode() {
        val nodes = loadNodes("backend/golang_GinController.json")
        val javaApiAnalyser = GoApiAnalyser()
        nodes.forEach {
            javaApiAnalyser.analysisByNode(it, "")
        }

        val services = javaApiAnalyser.toContainerServices()
        assertEquals(3, services[0].resources.size)
        assertEquals("/v1/users/", services[0].resources[0].sourceUrl)
        assertEquals("GET", services[0].resources[0].sourceHttpMethod)

        assertEquals("/v1/users/", services[0].resources[1].sourceUrl)
        assertEquals("POST", services[0].resources[1].sourceHttpMethod)

        assertEquals("/v1/users/:name", services[0].resources[2].sourceUrl)
        assertEquals("GET", services[0].resources[2].sourceHttpMethod)
    }

    @Test
    fun analysisHelloWorld() {
        val nodes = loadNodes("backend/golang_HelloWorld.json")
        val analyser = GoApiAnalyser()
        nodes.forEach {
            analyser.analysisByNode(it, "")
        }

        val services = analyser.toContainerServices()
        assertEquals(1, services[0].resources.size)
        assertEquals("/", services[0].resources[0].sourceUrl)
        assertEquals("GET", services[0].resources[0].sourceHttpMethod)
    }

    @Test
    fun analysisGoMuxFramework() {
        @Language("Go")
        val code = """
package main
import (
  "github.com/gorilla/mux"
)


func main() {
    r := mux.NewRouter()
    r.HandleFunc("/", HomeHandler).Methods("POST")
    r.HandleFunc("/products", ProductsHandler).Methods("GET", "POST")
    r.HandleFunc("/articles", ArticlesHandler).Methods("POST")
    http.Handle("/", r)
}
       """.trimIndent()
        val nodes = chapi.ast.goast.GoAnalyser().analysis(code, "main.go").DataStructures

        val analyser = GoApiAnalyser()
        nodes.forEach {
            analyser.analysisByNode(it, "")
        }

        val services = analyser.toContainerServices()
        assertEquals(4, services[0].resources.size)
        assertEquals("/", services[0].resources[0].sourceUrl)
        assertEquals("POST", services[0].resources[0].sourceHttpMethod)

        assertEquals("/products", services[0].resources[1].sourceUrl)
        assertEquals("GET", services[0].resources[1].sourceHttpMethod)


        // the get of products
        assertEquals("/products", services[0].resources[2].sourceUrl)
        assertEquals("POST", services[0].resources[2].sourceHttpMethod)
    }
}