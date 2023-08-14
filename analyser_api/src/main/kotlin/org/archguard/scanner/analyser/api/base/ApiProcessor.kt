package org.archguard.scanner.analyser.api.base

import org.archguard.scanner.core.openapi.ApiCollection

interface ApiProcessor {
    fun convertApi(): List<ApiCollection>
}