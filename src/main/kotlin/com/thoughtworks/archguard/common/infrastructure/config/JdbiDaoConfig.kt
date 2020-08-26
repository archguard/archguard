package com.thoughtworks.archguard.common.infrastructure.config

import com.thoughtworks.archguard.metrics.infrastructure.ClassCouplingDtoDaoForUpdate
import com.thoughtworks.archguard.metrics.infrastructure.ClassCouplingDtoDaoForRead
import com.thoughtworks.archguard.metrics.infrastructure.ClassMetricsDaoLegacy
import com.thoughtworks.archguard.metrics.infrastructure.ModuleMetricsDao
import com.thoughtworks.archguard.metrics.infrastructure.PackageMetricsDao
import com.thoughtworks.archguard.qualitygate.infrastructure.ProfileDao
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class JdbiDaoConfig {

    @Bean
    fun moduleMetricsDao(jdbi: Jdbi): ModuleMetricsDao {
        return jdbi.onDemand(ModuleMetricsDao::class.java)
    }

    @Bean
    fun packageMetricsDao(jdbi: Jdbi): PackageMetricsDao {
        return jdbi.onDemand(PackageMetricsDao::class.java)
    }

    @Bean
    fun classMetricsDao(jdbi: Jdbi): ClassMetricsDaoLegacy {
        return jdbi.onDemand(ClassMetricsDaoLegacy::class.java)
    }

    @Bean
    fun profileDao(jdbi: Jdbi): ProfileDao {
        return jdbi.onDemand(ProfileDao::class.java)
    }

    @Bean
    fun classCouplingDtoDaoForInsert(jdbi: Jdbi): ClassCouplingDtoDaoForUpdate {
        return jdbi.onDemand(ClassCouplingDtoDaoForUpdate::class.java)
    }

    @Bean
    fun classCouplingDtoDaoForRead(jdbi: Jdbi): ClassCouplingDtoDaoForRead {
        return jdbi.onDemand(ClassCouplingDtoDaoForRead::class.java)
    }

}
