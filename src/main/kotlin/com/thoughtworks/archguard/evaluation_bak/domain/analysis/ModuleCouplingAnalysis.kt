package com.thoughtworks.archguard.evaluation_bak.domain.analysis

import com.thoughtworks.archguard.evaluation_bak.domain.ModuleCouplingQuality
import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.ModuleCouplingQualityReport
import com.thoughtworks.archguard.evaluation_bak.domain.analysis.report.Report
import com.thoughtworks.archguard.report_bak.infrastructure.HotSpotRepo
import com.thoughtworks.archguard.report_bak.infrastructure.ClassStatisticRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ModuleCouplingAnalysis(@Autowired val classStatisticRepo: ClassStatisticRepo,
                             @Autowired val hotSpotRepo: HotSpotRepo) : Analysis {
    override fun getName(): String {
        return "模块耦合"
    }

    override fun getQualityReport(): Report? {
        return ModuleCouplingQualityReport(getModuleCouplingQuality())
    }

    private fun getLatestModule(): List<String> {
        return hotSpotRepo.queryLatestHotSpotPath(100).map { it.replace("/", ".") }
    }

    private fun getModuleInstability(fanout: Int, fanin: Int): Double {
        if (fanout == 0) {
            return 0.0
        }
        return fanout.toDouble() / (fanout + fanin)
    }

    private fun getModuleCouplingQuality(): List<ModuleCouplingQuality> {
        val fanInFanOut = classStatisticRepo.getModuleFanInFanOut()
        val result = HashMap(initMap(fanInFanOut.map { it.packageName }, getLatestModule()))
        fanInFanOut.forEach {
            val key = getKeyLike(result.keys, it.packageName)
            if (key != null) {
                val fanOutsum: Int = it.fanout.plus(result[key]?.first ?: 0)
                val fanInsum: Int = it.fanin.plus(result[key]?.second ?: 0)
                result[key] = Pair(fanOutsum, fanInsum)
            }
        }
        return result.map {
            ModuleCouplingQuality(it.key,
                    getModuleInstability(it.value.first, it.value.second),
                    it.value.second, it.value.first)
        }
    }

    private fun initMap(keys: List<String>, latestKeys: List<String>): Map<String, Pair<Int, Int>> {
        val grouped = ArrayList<String>(keys)
        keys.forEach {
            if (keys.filter { k -> k != it }.find { k -> it.startsWith(k) } != null) {
                grouped.remove(it)
            }
        }

        val latest = ArrayList<String>()
        for (latestKey in latestKeys) {
            val mappedKey = ArrayList(grouped).find { latestKey.contains(it) }
            if (mappedKey != null) {
                latest.add(mappedKey)
            }

        }

        return latest.associateBy({ it }, { Pair(0, 0) })
    }

    private fun getKeyLike(keys: Set<String>, key: String): String? {
        return keys.find { key.startsWith(it) }
    }
}
