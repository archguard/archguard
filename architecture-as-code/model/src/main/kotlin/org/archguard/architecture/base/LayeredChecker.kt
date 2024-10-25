package org.archguard.architecture.base

interface LayeredChecker {
    fun addToIdentify(str: String)
    fun canMarked(): Boolean
}