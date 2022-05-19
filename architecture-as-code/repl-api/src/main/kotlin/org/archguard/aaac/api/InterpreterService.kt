package org.archguard.aaac.api

import org.archguard.aaac.api.messaging.Message

interface InterpreterService {
    fun eval(interpreterRequest: InterpreterRequest): Message
}
