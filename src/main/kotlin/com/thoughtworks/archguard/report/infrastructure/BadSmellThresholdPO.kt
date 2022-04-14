package com.thoughtworks.archguard.report.infrastructure

import com.thoughtworks.archguard.report.domain.badsmell.ThresholdKey

class BadSmellThresholdPO(val name: String, val condition: String, val value: Int) {
    constructor(thresholdKey: ThresholdKey, value: Int) : this(thresholdKey.key, thresholdKey.condition, value)
}
