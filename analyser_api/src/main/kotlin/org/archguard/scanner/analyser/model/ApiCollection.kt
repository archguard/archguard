package org.archguard.scanner.analyser.model

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
    val response: List<Response> = listOf()
) {
    override fun toString(): String {
        val request = request.toString()
        val response = response.joinToString(", ") { it.toString() }
        return "$method $path $description $request $response"
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
}

@Serializable
data class Response(
    val code: Int,
    val parameters: List<Parameter> = listOf(),
    var bodyMode: BodyMode = BodyMode.TYPED,
    var bodyString: String = "",
) {
    override fun toString(): String = when (bodyMode) {
        BodyMode.RAW_TEXT -> {
            // TODO: 256 is a magic number
            if (bodyString.length > 256) {
                "$code: {}"
            } else {
                "$code: ${bodyString.replace("\r\n", "").replace("\n", "")}"
            }
        }

        BodyMode.TYPED -> {
            if (parameters.isEmpty()) {
                "$code: {}"
            } else {
                "$code: {${parameters.joinToString(", ") { it.toString() }}}"
            }
        }
    }
}

@Serializable
data class ApiTagOutput(val string: String) {
    override fun toString() = string
}
