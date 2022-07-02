package com.thoughtworks.archguard.v2.backyard.streamchannel

import io.mockk.*
import org.archguard.scanner.core.event.AnalyserEvent
import org.archguard.scanner.core.event.AnalyserEventType
import org.junit.jupiter.api.Test

internal class StreamChannelTest {

    @Test
    fun `should trigger related subscribers when publish a new event`() {
        val subscriber1 = mockk<AnalyserEventSubscriber> {
            every { this@mockk.type } returns AnalyserEventType.CODE_DATA_STRUCT
            every { process(any()) } just Runs
        }
        val subscriber2 = mockk<AnalyserEventSubscriber> {
            every { this@mockk.type } returns AnalyserEventType.API_CONTAINERS
            every { process(any()) } just Runs
        }
        val event = mockk<AnalyserEvent> {
            every { this@mockk.type } returns AnalyserEventType.CODE_DATA_STRUCT
        }
        val streamChannel = InMemoryStreamChannel(listOf(subscriber1, subscriber2))

        streamChannel.publish(event)

        verify {
            subscriber1.process(event)
        }
        verify(inverse = true) {
            subscriber2.process(any())
        }
    }
}