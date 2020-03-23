package com.thoughtworks.archgard.scanner.domain.hubexecutor

interface HubLifecycle {
    fun execute()

    fun clean()
}