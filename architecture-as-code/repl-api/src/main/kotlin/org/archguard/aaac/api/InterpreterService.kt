package org.archguard.aaac.api

interface InterpreterService {
    fun eval(interpreterRequest: InterpreterRequest): InterpreterResult
}
