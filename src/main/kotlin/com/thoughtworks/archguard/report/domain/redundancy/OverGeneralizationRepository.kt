package com.thoughtworks.archguard.report.domain.redundancy

interface OverGeneralizationRepository {
    fun getOverGeneralizationCount(systemId: Long): Long
    fun getOverGeneralizationParentClassId(systemId: Long): List<String>
    fun getOverGeneralizationPairList(parentClassIds: List<String>, limit: Long, offset: Long): List<OverGeneralizationPair>
}
