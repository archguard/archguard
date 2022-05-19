package org.archguard.aaac.client.websocket

interface MessageHandler {
    fun onMessage(msg: String)
}