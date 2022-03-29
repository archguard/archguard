package org.archguard.scanner.sourcecode.frontend.identify

import org.archguard.scanner.common.container.ContainerDemand
import chapi.domain.core.CodeCall
import chapi.domain.core.CodeImport

class UmiHttpIdentify : HttpIdentify {
    override fun isMatch(call: CodeCall, imports: Array<CodeImport>): Boolean {
        val imps = imports.filter { it.Source == "umi-request" }
        if (imps.isEmpty()) {
            return false
        }

        if (call.FunctionName == "request") {
            return true
        }

        return false
    }

    override fun convert(call: CodeCall): ContainerDemand {
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
