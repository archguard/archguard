package com.thoughtworks.archguard.systeminfo.domain

import org.slf4j.LoggerFactory

class DummyAnalysisClientProxy : AnalysisClientProxy {
    private val log = LoggerFactory.getLogger(DummyAnalysisClientProxy::class.java)
    override fun refreshThresholdCache() {
        log.info("In dummy client proxy")
    }
}
