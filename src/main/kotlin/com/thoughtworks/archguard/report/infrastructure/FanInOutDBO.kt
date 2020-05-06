package com.thoughtworks.archguard.report.infrastructure

data class FanInOutDBO(val packageName: String, val fanin: Int, val fanout: Int) {

}
