package com.thoughtworks.archguard.common.infrastructure.config

import com.thoughtworks.archguard.metrics.infrastructure.ClassCouplingDtoDaoForInsert
import com.thoughtworks.archguard.metrics.infrastructure.ClassCouplingDtoDaoForRead
import com.thoughtworks.archguard.metrics.infrastructure.ClassMetricsDao
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
    fun classMetricsDao(jdbi: Jdbi): ClassMetricsDao {
        return jdbi.onDemand(ClassMetricsDao::class.java)
    }

    @Bean
    fun profileDao(jdbi: Jdbi): ProfileDao {
        return jdbi.onDemand(ProfileDao::class.java)
    }

    @Bean
    fun classCouplingDtoDaoForInsert(jdbi: Jdbi): ClassCouplingDtoDaoForInsert {
        return jdbi.onDemand(ClassCouplingDtoDaoForInsert::class.java)
    }

    @Bean
    fun classCouplingDtoDaoForRead(jdbi: Jdbi): ClassCouplingDtoDaoForRead {
        return jdbi.onDemand(ClassCouplingDtoDaoForRead::class.java)
    }

}
