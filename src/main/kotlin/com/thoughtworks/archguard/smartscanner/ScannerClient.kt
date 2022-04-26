package com.thoughtworks.archguard.smartscanner

interface ScannerClient {
    fun send(command: ScannerCommand)
}

