package com.thoughtworks.archguard.codetree

interface InitCodeTreeService {
    fun initCodeTree(systemId: Long): CodeTree
}
