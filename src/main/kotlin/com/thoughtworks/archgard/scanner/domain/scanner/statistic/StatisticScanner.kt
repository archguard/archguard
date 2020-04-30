package com.thoughtworks.archgard.scanner.domain.scanner.statistic

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.DesigniteJavaTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StatisticScanner(@Autowired val statisticRepo: StatisticRepo) : Scanner {

    private val log = LoggerFactory.getLogger(StatisticScanner::class.java)
    override fun getScannerName(): String {
        return "Statistic"
    }

    override fun toolListGenerator(): List<ToolConfigure> {
        val result = ArrayList<ToolConfigure>()
        val config = HashMap<String, String>()
        config["available"] = "false"
        result.add(ToolConfigure(getScannerName(), config))
        return result
    }

    override fun scan(context: ScanContext) {
        log.info("start scan statistic report")
        val statistics = getStatistic(context)
        statisticRepo.delete()
        statisticRepo.save(statistics)
        log.info("finished scan statistic report")
    }

    private fun getStatistic(context: ScanContext): List<Statistic> {
        val designiteJavaTool = DesigniteJavaTool(context.workspace)
        return designiteJavaTool.getTypeMetricsReport().map { toStatistic(it) }

    }

    private fun toStatistic(line: String): Statistic {
        val elements = line.split(",")
        return Statistic(UUID.randomUUID().toString(), elements[0], elements[1], elements[2],
                elements[7].toInt(), elements[12].toInt(), elements[13].toInt())
    }
}