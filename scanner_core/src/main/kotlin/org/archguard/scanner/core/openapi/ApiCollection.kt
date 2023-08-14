package org.archguard.scanner.core.openapi

import kotlinx.serialization.Serializable

@Serializable
data class ApiCollection(
    val name: String,
    val description: String,
    val items: List<ApiItem>,
) {
    override fun toString(): String {
        return "$name: ${items.joinToString(", ") { it.toString() }}"
    }
}

@Serializable
data class ApiItem(
    val path: String,
    val method: String,
    var description: String,
    val operationId: String,
    val tags: List<String>,
    val request: Request? = null,
    val response: List<Response> = listOf(),
    var displayText: String = "",
) {
    override fun toString(): String {
        return this.renderDisplayText()
    }

    fun renderDisplayText(): String {
        val sb = StringBuilder()
        sb.append("### $description\n")
        sb.append("$method $path")

        val parameters = request?.parameters
        if (parameters?.isNotEmpty() == true) {
            val query = parameters.joinToString("&") { "${it.name}=${it.type}" }
            sb.append("?$query")
        }

        val body = request?.body
        if (body?.isNotEmpty() == true) {
            sb.append("\nRequest Body: [\n")
            sb.append(request!!.bodyString())
            sb.append("\n]")
        }

        val response = response.joinToString(", ") { it.toString() }
        if (response.isNotEmpty()) {
            sb.append("\nResponse Body: ")
            sb.append(response)
        }

        this.displayText = sb.toString()
        return this.displayText
    }
}

@Serializable
data class Parameter(
    val name: String,
    val type: String,
) {
    override fun toString() = "$name: $type"
}

enum class BodyMode {
    RAW_TEXT,
    TYPED,
}

@Serializable
data class Request(
    val parameters: List<Parameter> = listOf(),
    val body: List<Parameter> = listOf(),
    val bodyMode: BodyMode = BodyMode.TYPED,
    var bodyString: String = "",
) {
    override fun toString(): String {
        val params = parameters.joinToString(", ") { it.toString() }
        val body = body.joinToString(", ") { it.toString() }
        if (params.isEmpty() && body.isEmpty()) return ""
        if (params.isEmpty()) return body
        if (body.isEmpty()) return params

        return "$params, ($body)"
    }

    fun bodyString(): String {
        val body = body.joinToString(", ") { it.toString() }
        return "($body)"
    }
}

@Serializable
data class Response(
    val status: Int,
    val parameters: List<Parameter> = listOf(),
    var bodyMode: BodyMode = BodyMode.TYPED,
    var bodyString: String = "",
) {
    override fun toString(): String = when (bodyMode) {
        BodyMode.RAW_TEXT -> {
            // TODO: 256 is a magic number
            if (bodyString.length > 256) {
                "$status: {}"
            } else {
                "$status: ${bodyString.replace("\r\n", "").replace("\n", "")}"
            }
        }

        BodyMode.TYPED -> {
            if (parameters.isEmpty()) {
                "$status: {}"
            } else {
                "$status: {${parameters.joinToString(", ") { it.toString() }}}"
            }
        }
    }
}

@Serializable
data class ApiTagOutput(val string: String) {
    override fun toString() = string
}
