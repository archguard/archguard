package com.thoughtworks.archguard.v2.backyard.streamchannel

import org.archguard.scanner.core.event.AnalyserEvent
import org.archguard.scanner.core.event.AnalyserEventType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

open class StreamChannel {
    private var subscribers: MutableMap<AnalyserEventType, MutableList<AnalyserEventSubscriber>> = mutableMapOf()

    fun subscribe(subscriber: AnalyserEventSubscriber) {
        subscribers.getOrPut(subscriber.type) { mutableListOf() }.add(subscriber)
    }

    fun publish(event: AnalyserEvent) {
        subscribers[event.type]?.forEach { it.process(event) }
    }
}

// TODO implement stream channel with external MQ
@Component
class InMemoryStreamChannel(
    @Autowired private val _subscribers: List<AnalyserEventSubscriber>
) : StreamChannel() {
    init {
        _subscribers.forEach(::subscribe)
    }
}

interface AnalyserEventSubscriber {
    val type: AnalyserEventType
    fun process(raw: AnalyserEvent)
}
