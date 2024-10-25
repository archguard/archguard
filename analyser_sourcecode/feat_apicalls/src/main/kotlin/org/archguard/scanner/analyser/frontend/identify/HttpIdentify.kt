package org.archguard.scanner.analyser.frontend.identify

import chapi.domain.core.CodeCall
import chapi.domain.core.CodeImport
import org.archguard.scanner.analyser.frontend.ApiCodeCall
import org.archguard.context.ContainerDemand

interface HttpIdentify {
    fun isMatch(call: CodeCall, imports: List<CodeImport>): Boolean
    fun convert(call: ApiCodeCall): ContainerDemand
}
