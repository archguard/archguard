package org.archguard.scanner.analyser.api.base

import org.archguard.model.ApiCollection

interface ApiProcessor {
    fun convertApi(): List<ApiCollection>
}