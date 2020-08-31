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
class StatisticScanner(@Autowired val classClassStatisticRepo: ClassStatisticRepo,
                       @Autowired val methodClassStatisticRepo: MethodStatisticRepo) : Scanner {

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
        classClassStatisticRepo.delete()
        classClassStatisticRepo.save(statistics)
        methodClassStatisticRepo.delete()
        methodClassStatisticRepo.save(getMethodStatistic(context))
        log.info("finished scan statistic report")
    }

    private fun getStatistic(context: ScanContext): List<ClassStatistic> {
        val designiteJavaTool = DesigniteJavaTool(context.workspace)
        return designiteJavaTool.getTypeMetricsReport().map { toStatistic(it) }

    }

    private fun getMethodStatistic(context: ScanContext): List<MethodStatistic> {
        val designiteJavaTool = DesigniteJavaTool(context.workspace)
        return designiteJavaTool.getMethodMetricsReport().map { toMethodStatistic(it) }

    }

    private fun toStatistic(line: String): ClassStatistic {
        val elements = line.split(",")
        return ClassStatistic(UUID.randomUUID().toString(), elements[0], elements[1], elements[2],
                elements[7].toInt(), elements[12].toInt(), elements[13].toInt())
    }

    private fun toMethodStatistic(line: String): MethodStatistic {
        val elements = line.split(",")
        return MethodStatistic(UUID.randomUUID().toString(), elements[1], elements[2],
                elements[3], elements[4].toInt())
    }
}