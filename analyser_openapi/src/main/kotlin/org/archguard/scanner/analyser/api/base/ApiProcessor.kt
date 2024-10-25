package org.archguard.scanner.analyser.api.base

import org.archguard.context.ApiCollection

interface ApiProcessor {
    fun convertApi(): List<ApiCollection>
}