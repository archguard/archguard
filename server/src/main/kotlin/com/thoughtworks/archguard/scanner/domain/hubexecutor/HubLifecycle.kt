package com.thoughtworks.archguard.scanner.domain.hubexecutor

interface HubLifecycle {
    fun execute()

    fun clean()
}
