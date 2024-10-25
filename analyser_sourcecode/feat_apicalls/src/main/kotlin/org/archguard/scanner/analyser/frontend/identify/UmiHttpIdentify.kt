package org.archguard.scanner.analyser.frontend.identify

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeImport
import org.archguard.scanner.analyser.frontend.ApiCodeCall
import org.archguard.context.ContainerDemand

class UmiHttpIdentify : HttpIdentify {
    override fun isMatch(call: CodeCall, imports: List<CodeImport>): Boolean {
        val imps = imports.filter { it.Source == "umi-request" }
        if (imps.isEmpty()) {
            return false
        }

        if (call.FunctionName == "request") {
            return true
        }

        return false
    }

    override fun convert(call: ApiCodeCall): ContainerDemand {
        val url = call.Parameters[0].TypeValue
        val httpApi = ContainerDemand(target_url = url)

        for (codeProperty in call.Parameters[1].ObjectValue) {
            when (codeProperty.TypeValue) {
                "method" -> {
                    httpApi.target_http_method = codeProperty.ObjectValue[0].TypeValue
                }

                "data" -> {
                    httpApi.call_data = codeProperty.ObjectValue[0].TypeValue
                }
            }
        }

        return httpApi
    }
}
