package com.thoughtworks.archguard.report_bak.infrastructure

data class FanInOutDBO(val packageName: String, val fanin: Int, val fanout: Int) {

}
