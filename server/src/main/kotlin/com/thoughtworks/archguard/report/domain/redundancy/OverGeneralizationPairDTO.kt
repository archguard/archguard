package com.thoughtworks.archguard.report.domain.redundancy

data class OverGeneralizationPairDTO(
    val parentModuleName: String,
    val parentPackageName: String,
    val parentClassName: String,
    val childModuleName: String,
    val childPackageName: String,
    val childClassName: String
) {
    companion object {
        fun create(pair: OverGeneralizationPair): OverGeneralizationPairDTO {
            return OverGeneralizationPairDTO(
                pair.parentClass.moduleName,
                pair.parentClass.packageName, pair.parentClass.className,
                pair.childClass.moduleName, pair.childClass.packageName, pair.childClass.className
            )
        }
    }
}

data class OverGeneralizationPairListDTO(
    val data: List<OverGeneralizationPairDTO>,
    val count: Long,
    val currentPageNumber: Long
)
