package com.thoughtworks.archguard.report.domain.badsmell

import org.springframework.context.annotation.DependsOn
import org.springframework.stereotype.Service

@Service
@DependsOn("flywayInitializer")
class ThresholdSuiteServiceWithDepends(thresholdSuiteRepository: ThresholdSuiteRepository) :
    ThresholdSuiteService(thresholdSuiteRepository)
