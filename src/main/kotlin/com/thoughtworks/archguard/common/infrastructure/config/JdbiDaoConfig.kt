package com.thoughtworks.archguard.common.infrastructure.config

import com.thoughtworks.archguard.metrics.infrastructure.ClassCouplingReadDao
import com.thoughtworks.archguard.metrics.infrastructure.ClassCouplingWriteDao
import com.thoughtworks.archguard.qualitygate.infrastructure.ProfileDao
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class JdbiDaoConfig {

    @Bean
    fun classCouplingDtoDaoForUpdate(jdbi: Jdbi): ClassCouplingWriteDao {
        return jdbi.onDemand(ClassCouplingWriteDao::class.java)
    }

    @Bean
    fun classCouplingDtoDaoForRead(jdbi: Jdbi): ClassCouplingReadDao {
        return jdbi.onDemand(ClassCouplingReadDao::class.java)
    }

    @Bean
    fun profileDao(jdbi: Jdbi): ProfileDao {
        return jdbi.onDemand(ProfileDao::class.java)
    }
}
