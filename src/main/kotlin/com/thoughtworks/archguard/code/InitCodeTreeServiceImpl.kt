package com.thoughtworks.archguard.code

import com.thoughtworks.archguard.clazz.domain.JClassRepository
import org.springframework.stereotype.Service

@Service
class InitCodeTreeServiceImpl(val jClassRepository: JClassRepository) : InitCodeTreeService {
    override fun initCodeTree(): CodeTree {
        val codeTree: CodeTree = CodeTree()
        jClassRepository.getAll().filter { it.module != "null" }.map { codeTree.addClass(it.getFullName()) }
        return codeTree
    }

}