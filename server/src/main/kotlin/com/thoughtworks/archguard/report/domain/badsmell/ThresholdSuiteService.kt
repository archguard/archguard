package com.thoughtworks.archguard.report.domain.badsmell

import com.thoughtworks.archguard.report.exception.ThresholdNotDefinedException
import org.archguard.threshold.ThresholdKey
import org.archguard.threshold.ThresholdSuite
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
@DependsOn("flywayInitializer")
class ThresholdSuiteService(val thresholdSuiteRepository: ThresholdSuiteRepository) {

    private val logger = LoggerFactory.getLogger(ThresholdSuiteService::class.java)

    lateinit var allBadSmellThresholdSuites: List<ThresholdSuite>

    @PostConstruct
    fun initThresholdCache() {
        allBadSmellThresholdSuites = thresholdSuiteRepository.getAllBadSmellThresholdSuites()
        allBadSmellThresholdSuites.forEach {
            logger.info("Loaded suite " + it.id + " - " + it.suiteName + " using by system " + it.systemIds)
        }
    }

    fun reloadAllSuites() {
        allBadSmellThresholdSuites = thresholdSuiteRepository.getAllBadSmellThresholdSuites()
        logger.info("Reloaded bad smell threshold suites with entry {}", allBadSmellThresholdSuites.size)
    }

    fun getThresholdValue(systemId: Long, key: ThresholdKey): Int {
        val newThreshold = allBadSmellThresholdSuites.filter { it.systemIds.contains(systemId) }
        if (newThreshold.isNotEmpty()) {
            return newThreshold[0].getValue(key)
        }

        // if no load from cache, reload again
        this.reloadAllSuites()
        try {
            return allBadSmellThresholdSuites.first { it.systemIds.contains(systemId) }.getValue(key)
        } catch (ex: NoSuchElementException) {
            throw ThresholdNotDefinedException("Threshold with key ${key.name} is not defined for $systemId.")
        }
    }
}
