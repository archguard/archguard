package org.archguard.aaac.api.websocket

interface MessageHandler {
    fun onMessage(msg: String)
}