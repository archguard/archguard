package org.archguard.scanner.analyser.api.postman

import kotlinx.serialization.Serializable
import java.net.URLEncoder
import java.util.*

@Serializable
class PostmanRequest {
    var method: String? = null
    var header: List<PostmanHeader>? = null
    var body: PostmanBody? = null
    var url: PostmanUrl? = null
    var description: String? = null
    var auth: org.archguard.scanner.analyser.api.postman.PostmanAuth? = null

    fun getData(`var`: PostmanVariables): String? {
        return if (body == null || body!!.mode == null) {
            ""
        } else {
            when (body!!.mode) {
                "raw" -> `var`.replace(body!!.raw)
                "urlencoded" -> urlFormEncodeData(`var`, body!!.urlencoded)
                else -> ""
            }
        }
    }

    fun urlFormEncodeData(`var`: PostmanVariables, formData: List<PostmanUrlEncoded?>?): String {
        var result = ""
        val i = 0
        for (encoded in formData!!) {
            result += encoded!!.key + "=" + URLEncoder.encode(`var`.replace(encoded.value))
            if (i < formData.size - 1) {
                result += "&"
            }
        }
        return result
    }

    fun getUrl(`var`: PostmanVariables): String? {
        if (url == null) {
            return null
        }

        return `var`.replace(url!!.raw)
    }

    fun getHeaders(`var`: PostmanVariables): Map<String?, String?> {
        val result: MutableMap<String?, String?> = HashMap()
        if (header == null || header!!.isEmpty()) {
            return result
        }
        for (head in header!!) {
            if (head.key!!.uppercase(Locale.getDefault()) == PoyntHttpHeaders.REQUEST_ID_HEADER) {
                result[head.key!!.uppercase(Locale.getDefault())] = `var`.replace(head.value)
            } else {
                result[head.key] = `var`.replace(head.value)
            }
        }
        return result
    }
}
