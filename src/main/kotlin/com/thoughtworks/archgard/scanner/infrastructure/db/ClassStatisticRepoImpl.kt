package com.thoughtworks.archgard.scanner.infrastructure.db

import com.thoughtworks.archgard.scanner.domain.scanner.statistic.ClassStatistic
import com.thoughtworks.archgard.scanner.domain.scanner.statistic.ClassStatisticRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository

@Repository
class ClassStatisticRepoImpl(@Autowired private val classStatisticDao: ClassStatisticDao) : ClassStatisticRepo {
    override fun save(classStatistic: List<ClassStatistic>) {
        classStatisticDao.saveAll(classStatistic)
    }

    override fun delete() {
        classStatisticDao.deleteAll()
    }

}