package com.thoughtworks.archgard.scanner.domain.scanner.statistic

import com.thoughtworks.archgard.scanner.domain.ScanContext
import com.thoughtworks.archgard.scanner.domain.scanner.Scanner
import com.thoughtworks.archgard.scanner.domain.tools.DesigniteJavaTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StatisticScanner(@Autowired val statisticRepo: StatisticRepo) : Scanner {

    private val log = LoggerFactory.getLogger(StatisticScanner::class.java)


    override fun scan(context: ScanContext) {
        log.info("start scan statistic report")
        val statistics = getStatistic(context)
        statisticRepo.save(statistics)
        statisticRepo.delete()
        log.info("finished scan statistic report")
    }

    private fun getStatistic(context: ScanContext): List<Statistic> {
        val designiteJavaTool = DesigniteJavaTool(context.workspace)
        val statisticReport = designiteJavaTool.getTypeMetricsReport()
        val lines = statisticReport?.readLines()
        val readLines = lines?.subList(1, lines.size).orEmpty()

        val result = ArrayList<Statistic>()
        for (line in readLines) {
            val elements = line.split(",")
            result.add(Statistic(UUID.randomUUID().toString(), elements[0], elements[1], elements[2], elements[7].toInt()))
        }
        return result
    }
}