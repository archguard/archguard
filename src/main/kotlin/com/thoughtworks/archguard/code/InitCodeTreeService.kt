package com.thoughtworks.archguard.code

interface InitCodeTreeService {
    fun initCodeTree(projectId: Long): CodeTree
}
