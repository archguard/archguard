package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.statistic.Statistic
import com.thoughtworks.archgard.scanner.domain.scanner.statistic.StatisticRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class StatisticRepoImpl(@Autowired private val statisticDao: StatisticDao) : StatisticRepo {
    override fun save(statistic: List<Statistic>) {
        statisticDao.saveAll(statistic)
    }

    override fun delete() {
        statisticDao.deleteAll()
    }

}