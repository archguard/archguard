package org.archguard.scanner.core.openapi;

import org.archguard.context.*
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test

class ApiCollectionTest {
    @Test
    fun should_return_status_and_empty_parameters_when_bodyMode_is_TYPED_and_parameters_is_empty() {
        // given
        val response = Response(200, emptyList(), BodyMode.TYPED)

        // when
        val result = response.toString()

        // then
        assertEquals("200: {}", result)
    }


    @Test
    fun should_return_status_and_parameters_when_bodyMode_is_TYPED_and_parameters_is_not_empty() {
        // given
        val parameters = listOf(Parameter("param1", "value1"), Parameter("param2", "value2"))
        val response = Response(200, parameters, BodyMode.TYPED)

        // when
        val result = response.toString()

        // then
        assertEquals("200: {param1: value1, param2: value2}", result)
    }

    @Test
    fun `given empty parameters and body, when calling toString(), then return empty string`() {
        // given
        val request = Request()

        // when
        val result = request.toString()

        // then
        assertEquals("", result)
    }

    @Test
    fun `given non-empty parameters and non-empty body, when calling toString(), then return combined string`() {
        // given
        val parameters = listOf(Parameter("param1", "value1"), Parameter("param2", "value2"))
        val body = listOf(Parameter("bodyParam1", "bodyValue1"), Parameter("bodyParam2", "bodyValue2"))
        val request = Request(parameters = parameters, body = body)

        // when
        val result = request.toString()

        // then
        assertEquals("param1: value1, param2: value2, (bodyParam1: bodyValue1, bodyParam2: bodyValue2)", result)
    }

    @Test
    fun `given empty body, when calling bodyString(), then return empty string`() {
        // given
        val request = Request()

        // when
        val result = request.bodyString()

        // then
        assertEquals("()", result)
    }

    @Test
    fun `given non-empty body, when calling bodyString(), then return body string`() {
        // given
        val body = listOf(Parameter("bodyParam1", "bodyValue1"), Parameter("bodyParam2", "bodyValue2"))
        val request = Request(body = body)

        // when
        val result = request.bodyString()

        // then
        assertEquals("(bodyParam1: bodyValue1, bodyParam2: bodyValue2)", result)
    }

    @Test
    fun shouldRenderDisplayTextWithOperationIdAndDescription() {
        // given
        val apiItem = ApiItem(
            path = "/users",
            method = "GET",
            description = "Get all users",
            operationId = "getAllUsers",
            tags = listOf("users")
        )

        // when
        val displayText = apiItem.renderDisplayText()

        // then
        assertEquals("### getAllUsers\n> Get all users\nGET /users", displayText)
    }

    @Test
    fun shouldRenderDisplayTextWithoutOperationIdAndDescription() {
        // given
        val apiItem = ApiItem(
            path = "/users",
            method = "GET",
            description = "",
            operationId = "",
            tags = listOf("users")
        )

        // when
        val displayText = apiItem.renderDisplayText()

        // then
        assertEquals("GET /users", displayText)
    }

    @Test
    fun shouldRenderDisplayTextWithRequestParameters() {
        // given
        val parameters = listOf(
            Parameter(name = "page", type = "Int"),
            Parameter(name = "size", type = "Int")
        )
        val request = Request(parameters = parameters)
        val apiItem = ApiItem(
            path = "/users",
            method = "GET",
            description = "Get all users",
            operationId = "getAllUsers",
            tags = listOf("users"),
            request = request
        )

        // when
        val displayText = apiItem.renderDisplayText()

        // then
        assertEquals("### getAllUsers\n> Get all users\nGET /users?page=Int&size=Int", displayText)
    }

    @Test
    fun shouldRenderDisplayTextWithoutResponse() {
        // given
        val apiItem = ApiItem(
            path = "/users",
            method = "GET",
            description = "Get all users",
            operationId = "getAllUsers",
            tags = listOf("users")
        )

        // when
        val displayText = apiItem.renderDisplayText()

        // then
        assertEquals("### getAllUsers\n> Get all users\nGET /users", displayText)
    }
}