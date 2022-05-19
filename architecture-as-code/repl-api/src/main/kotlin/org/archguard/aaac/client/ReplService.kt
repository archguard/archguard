package org.archguard.aaac.client

interface ReplService {
    fun eval(evalRequest: EvalRequest): ExecuteResult
}
