package org.archguard.scanner.analyser.api.base

import org.archguard.scanner.analyser.model.ApiCollection

interface ApiProcessor {
    fun convertApi(): List<ApiCollection>
}