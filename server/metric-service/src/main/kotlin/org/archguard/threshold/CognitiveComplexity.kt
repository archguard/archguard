package org.archguard.threshold

import org.archguard.model.ChangeEntry

class CognitiveComplexity(
    val commitId: String,
    val changedCognitiveComplexity: Int,
    val systemId: Long,
    val path: String
) {

    companion object {
        fun from(path: String, changeEntryList: List<ChangeEntry>, systemId: Long): List<CognitiveComplexity> {
            val result = ArrayList<CognitiveComplexity>()
            val sorted = changeEntryList.sortedBy { it.commitTime }
            if (sorted[0].changeMode == "ADD") {
                result.add(CognitiveComplexity(sorted[0].commitId, sorted[0].cognitiveComplexity, systemId, sorted[0].newPath))
            }
            for (x in 1 until sorted.size) {
                val changedCognitiveComplexity = Math.abs(sorted[x].cognitiveComplexity - sorted[x - 1].cognitiveComplexity)
                result.add(CognitiveComplexity(sorted[x].commitId, changedCognitiveComplexity, systemId, path))
            }
            return result
        }
    }
}
