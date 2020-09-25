package com.thoughtworks.archguard.report.domain.overgeneralization

import com.thoughtworks.archguard.report.domain.module.ClassVO

interface OverGeneralizationRepository {
    fun getOverGeneralizationCount(systemId: Long): Long
    fun getOverGeneralizationList(systemId: Long, limit: Long, offset: Long): List<ClassVO>

}
