package com.thoughtworks.archguard.v2.frontier.method.infra

import com.thoughtworks.archguard.v2.backyard.streamchannel.AnalyserEventSubscriber
import com.thoughtworks.archguard.v2.frontier.method.domain.MethodRepository
import org.archguard.scanner.core.event.AnalyserEvent
import org.archguard.scanner.core.event.AnalyserEventType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

// implement of repository, also accept event as the input data
// move this into frontier folder
@Repository
class MethodRepositoryImpl(
    @Autowired private val dao: MethodDao,
) : MethodRepository, AnalyserEventSubscriber {
    override val type: AnalyserEventType = AnalyserEventType.CODE_DATA_STRUCT
    override fun process(raw: AnalyserEvent) {
        TODO("Not yet implemented")
    }
}