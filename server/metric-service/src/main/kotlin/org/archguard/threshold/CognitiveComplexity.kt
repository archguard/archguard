package org.archguard.threshold

import org.archguard.model.ChangeEntry
import kotlin.math.abs

class CognitiveComplexity(
    val commitId: String,
    val changedCognitiveComplexity: Int,
    val systemId: Long,
    val path: String
) {

    companion object {
        fun from(path: String, changeEntryList: List<ChangeEntry>, systemId: Long): List<CognitiveComplexity> {
            val result = mutableListOf<CognitiveComplexity>()
            val sorted = changeEntryList.sortedBy { it.commitTime }

            if (sorted.first().changeMode == "ADD") {
                result.add(CognitiveComplexity(sorted.first().commitId, sorted.first().cognitiveComplexity, systemId, sorted.first().newPath))
            }

            for (x in 1 until sorted.size) {
                val changedCognitiveComplexity = abs(sorted[x].cognitiveComplexity - sorted[x - 1].cognitiveComplexity)
                result.add(CognitiveComplexity(sorted[x].commitId, changedCognitiveComplexity, systemId, path))
            }

            return result
        }
    }
}
