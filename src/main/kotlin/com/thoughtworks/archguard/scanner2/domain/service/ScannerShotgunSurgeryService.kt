package com.thoughtworks.archguard.scanner2.domain.service

import com.thoughtworks.archguard.scanner2.domain.model.CognitiveComplexity
import com.thoughtworks.archguard.scanner2.domain.repository.ChangeEntryRepository
import com.thoughtworks.archguard.scanner2.domain.repository.CognitiveComplexityRepository
import org.springframework.stereotype.Service

@Service
class ScannerShotgunSurgeryService(val changeEntryRepository: ChangeEntryRepository,
                                   val cognitiveComplexityRepository: CognitiveComplexityRepository) {

    fun persist(systemId: Long) {
        val entryList = changeEntryRepository.getAllChangeEntry(systemId);

        val cognitiveComplexityList = ArrayList<CognitiveComplexity>()
        entryList.groupBy { if (it.newPath.equals("/dev/null")) it.oldPath else it.newPath }.forEach { (k, v) ->
            cognitiveComplexityList.addAll(CognitiveComplexity.from(k, v, systemId))
        }
        cognitiveComplexityRepository.saveAll(systemId, cognitiveComplexityList)
    }

}