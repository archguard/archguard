package com.thoughtworks.archguard.report.domain.redundancy

data class OverGeneralizationPairDTO(val pair: OverGeneralizationPair) {
    val parentModuleName: String = pair.parentClass.moduleName
    val parentPackageName: String = pair.parentClass.packageName
    val parentClassName: String = pair.parentClass.className

    val childModuleName: String = pair.childClass.moduleName
    val childPackageName: String = pair.childClass.packageName
    val childClassName: String = pair.childClass.className

}

data class OverGeneralizationPairListDTO(val data: List<OverGeneralizationPairDTO>,
                                         val count: Long, val currentPageNumber: Long)