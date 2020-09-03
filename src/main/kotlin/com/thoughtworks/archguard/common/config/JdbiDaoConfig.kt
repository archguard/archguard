package com.thoughtworks.archguard.common.config

import com.thoughtworks.archguard.qualitygate.infrastructure.ProfileDao
import org.jdbi.v3.core.Jdbi
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component

@Component
class JdbiDaoConfig {


    @Bean
    fun profileDao(jdbi: Jdbi): ProfileDao {
        return jdbi.onDemand(ProfileDao::class.java)
    }


}
