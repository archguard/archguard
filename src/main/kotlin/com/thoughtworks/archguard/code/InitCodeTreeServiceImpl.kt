package com.thoughtworks.archguard.code

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import org.springframework.stereotype.Service

@Service
class InitCodeTreeServiceImpl(val jClassRepository: JClassRepository) : InitCodeTreeService {
    override fun initCodeTree(): CodeTree {
        TODO("Not yet implemented")
    }

}