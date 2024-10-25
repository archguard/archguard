package org.archguard.scanner.analyser.api.openapi

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Operation
import io.swagger.v3.oas.models.media.Schema
import io.swagger.v3.oas.models.responses.ApiResponse
import io.swagger.v3.parser.OpenAPIV3Parser
import org.archguard.model.*
import org.archguard.scanner.analyser.api.base.ApiProcessor
import java.io.File

class OpenApiV3Processor(private val api: OpenAPI, val file: File) : ApiProcessor {
    private val apiSchemaMutableMap = api.components?.schemas

    override fun convertApi(): List<ApiCollection> {
        if (api.paths == null) return listOf()
        val allItems = mutableListOf<ApiItem>()

        api.paths.forEach { (path, pathItem) ->
            pathItem.readOperationsMap().forEach { (method, operation) ->
                val apiItem = ApiItem(
                    path = path,
                    method = method.toString(),
                    description = operation.description?.replace("\n", " ") ?: "",
                    operationId = operation.operationId ?: "",
                    tags = operation.tags ?: listOf(),
                    request = convertRequest(operation),
                    response = convertResponses(operation),
                )

                apiItem.renderDisplayText()

                allItems.add(apiItem)
            }
        }

        // group by tag
        val apiDetailsByTag = allItems.groupBy { it.tags.firstOrNull() ?: "" }
        return apiDetailsByTag.map { (tag, apiItems) ->
            ApiCollection(tag, file.name, "", apiItems)
        }
    }

    private fun convertResponses(operation: Operation): List<Response> {
        return operation.responses?.map {
            // use regex to get the status code
            val regex = Regex("([0-9]+)")
            val code = regex.find(it.key)?.value?.toInt() ?: 0
            val responseBody = handleResponse(it.value) ?: listOf()
            Response(code, responseBody)
        } ?: listOf()
    }

    private fun handleResponse(response: ApiResponse): List<Parameter>? {
        val content = response.content?.values
        val refName = content?.firstOrNull()?.schema?.`$ref`
        if (refName != null) {
            return getFromSchemaRef(refName)
        }

        val schema = content?.firstOrNull()?.schema
        if (schema != null) {
            return getFromSchemaItem(schema)
        }

        return null
    }

    private fun getFromSchemaItem(schema: Schema<Any>) =
        schema.properties?.map { (name, schema) ->
            Parameter(
                name = name,
                type = schema.type ?: "",
            )
        }

    private fun getFromSchemaRef(refName: String): List<Parameter>? {
        val name = refName.split("/").last()
        val schema = apiSchemaMutableMap?.get(name)
        return schema?.properties?.map { (name, schema) ->
            Parameter(
                name = name,
                type = schema.type ?: "",
            )
        }
    }

    private fun convertRequest(operation: Operation): Request {
        val parameters = operation.parameters?.map {
            Parameter(
                name = it.name ?: "",
                type = it.schema?.type ?: "",
            )
        }

        val request = operation.requestBody?.content?.values?.flatMap { content ->
            content.schema?.properties?.map { (name, schema) ->
                Parameter(
                    name = name,
                    type = schema.type ?: "",
                )
            } ?: listOf()
        } ?: listOf()

        return Request(parameters ?: listOf(), request)
    }

    companion object {
        private val logger = org.slf4j.LoggerFactory.getLogger(OpenApiV3Processor::class.java)!!

        fun fromFile(file: File): OpenAPI? {
            try {
                return OpenAPIV3Parser().read(file.absolutePath)
            } catch (e: Exception) {
                logger.error("parse swagger file failed: ${file.absolutePath}")
            }

            return null
        }
    }
}
