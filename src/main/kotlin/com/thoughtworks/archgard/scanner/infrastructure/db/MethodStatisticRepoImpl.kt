package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.statistic.MethodStatistic
import com.thoughtworks.archgard.scanner.domain.scanner.statistic.MethodStatisticRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class MethodStatisticRepoImpl(@Autowired private val methodStatisticDao: MethodStatisticDao) : MethodStatisticRepo {
    override fun save(methodStatistic: List<MethodStatistic>) {
        methodStatisticDao.saveAll(methodStatistic)
    }

    override fun delete() {
        methodStatisticDao.deleteAll()
    }

}