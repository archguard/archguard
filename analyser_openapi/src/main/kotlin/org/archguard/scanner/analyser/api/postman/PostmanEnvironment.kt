package org.archguard.scanner.analyser.api.postman

class PostmanEnvironment {
    var id: String? = null
    var name: String? = null
    var values: List<PostmanEnvValue>? = null
    var timestamp: Long? = null
    var synced: Boolean? = null
    var lookup: MutableMap<String?, PostmanEnvValue?> = HashMap()
    fun init() {
        for (`val` in values!!) {
            lookup[`val`.key] = `val`
        }
    }

    fun setEnvironmentVariable(key: String, value: String?) {
        val existingVar = lookup[key]
        if (existingVar != null) {
            // Update existing value if any
            existingVar.value = value
        } else {
            val newVar = PostmanEnvValue()
            newVar.key = key
            newVar.name = "RUNTIME-$key"
            newVar.type = "text"
            newVar.value = value
            lookup[key] = newVar
        }
    }
}
