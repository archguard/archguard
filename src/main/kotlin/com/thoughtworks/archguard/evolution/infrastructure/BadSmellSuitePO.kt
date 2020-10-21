package com.thoughtworks.archguard.evolution.infrastructure

import com.google.gson.Gson
import com.thoughtworks.archguard.evolution.domain.BadSmellGroup
import com.thoughtworks.archguard.evolution.domain.BadSmellSuite

class BadSmellSuitePO(val id: Long, val suiteName: String, val isDefault: Boolean, val thresholds: String) {
    fun toBadSmellSuite(): BadSmellSuite {
        val groups = Gson().fromJson(thresholds, Array<BadSmellGroup>::class.java).toList()
        return BadSmellSuite(id, suiteName, isDefault, groups)
    }
}
