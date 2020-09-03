package com.thoughtworks.archguard.report_bak.domain.model

data class TopItem(var itemName: String, var coverageRate: Float) {
    constructor() : this("", 0.0F)
}