package org.archguard.aaac.client

interface InterpreterService {
    fun eval(interpreterRequest: InterpreterRequest): InterpreterResult
}
