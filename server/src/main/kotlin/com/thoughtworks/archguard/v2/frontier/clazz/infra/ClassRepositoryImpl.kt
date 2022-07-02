package com.thoughtworks.archguard.v2.frontier.clazz.infra

import com.thoughtworks.archguard.v2.backyard.persistent.mongo.MongoClassDao
import com.thoughtworks.archguard.v2.backyard.streamchannel.AnalyserEventSubscriber
import com.thoughtworks.archguard.v2.frontier.clazz.domain.ClassRepository
import org.archguard.scanner.core.event.AnalyserEvent
import org.archguard.scanner.core.event.AnalyserEventType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

// implement of repository, also accept event as the input data
// move this into frontier folder
@Repository
class ClassRepositoryImpl (
    @Autowired private val dao: MongoClassDao,
): ClassRepository, AnalyserEventSubscriber {
    override val type: AnalyserEventType = AnalyserEventType.CODE_DATA_STRUCT
    override fun process(raw: AnalyserEvent) {
        TODO("Not yet implemented")
    }
}