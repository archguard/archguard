package org.archguard.scanner.core.openapi

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ApiItemTest {
    @Test
    fun testRenderDisplayText() {
        val apiItem = ApiItem(
            path = "/api/v1/health",
            method = "GET",
            description = "Get health status",
            operationId = "getHealth",
            tags = listOf("Health"),
            request = Request(
                parameters = listOf(
                    Parameter(
                        name = "id",
                        type = "string",
                    ),
                    Parameter(
                        name = "name",
                        type = "string",
                    ),
                ),
                body = listOf(
                    Parameter(
                        name = "body",
                        type = "string",
                    ),
                ),
                bodyMode = BodyMode.TYPED,
                bodyString = "body",
            ),
            response = listOf(
                Response(
                    status = 200,
                    parameters = listOf(
                        Parameter(
                            name = "body",
                            type = "string",
                        ),
                    ),
                    bodyMode = BodyMode.TYPED,
                    bodyString = "body",
                ),
            ),
            displayText = "",
        )

        val expected =
            """### Get health status
GET /api/v1/health?id=string&name=string
Request Body: [
(body: string)
]
Response Body: 200: {body: string}"""

        val actual = apiItem.renderDisplayText()

        assertEquals(expected, actual)
    }
}