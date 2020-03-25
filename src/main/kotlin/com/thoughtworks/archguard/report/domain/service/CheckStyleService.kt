package com.thoughtworks.archguard.report.domain.service

import com.thoughtworks.archguard.report.domain.dto.CheckStyleDTO
import com.thoughtworks.archguard.report.domain.repository.CheckStyleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

@Service
class CheckStyleService {

    @Autowired
    lateinit var checkStyleRepository: CheckStyleRepository

    fun getOverview(): List<CheckStyleDTO> =
            checkStyleRepository.getCheckStyleOverview()
                    .groupBy { it }
                    .map { CheckStyleDTO(it.key, it.value.size) }

}
