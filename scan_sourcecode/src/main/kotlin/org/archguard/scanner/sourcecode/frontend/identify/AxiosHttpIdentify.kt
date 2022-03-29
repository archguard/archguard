package org.archguard.scanner.sourcecode.frontend.identify

import org.archguard.scanner.sourcecode.container.ContainerDemand
import chapi.domain.core.CodeCall
import chapi.domain.core.CodeImport

open class AxiosHttpIdentify : HttpIdentify {
    override fun isMatch(call: CodeCall, imports: Array<CodeImport>): Boolean {
        if (call.FunctionName == "axios" || call.FunctionName.startsWith("axios.")) {
            if (call.Parameters.isNotEmpty()) {
                return true
            }
        }

        return false
    }

    override fun convert(call: CodeCall): ContainerDemand {
        val httpApi = ContainerDemand()
        call.Parameters.forEach { prop ->
            for (codeProperty in prop.ObjectValue) {
                when (codeProperty.TypeValue) {
                    "baseURL" -> {
                        httpApi.base = codeProperty.ObjectValue[0].TypeValue
                    }
                    "url" -> {
                        httpApi.target_url = codeProperty.ObjectValue[0].TypeValue
                    }
                    "method" -> {
                        httpApi.target_http_method = codeProperty.ObjectValue[0].TypeValue
                    }
                    "data" -> {
                        httpApi.call_data = codeProperty.ObjectValue[0].TypeValue
                    }
                }
            }
        }

        return httpApi
    }
}
