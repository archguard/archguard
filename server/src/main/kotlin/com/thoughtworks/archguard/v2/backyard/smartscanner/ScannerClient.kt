package com.thoughtworks.archguard.v2.backyard.smartscanner

interface ScannerClient {
    fun send(command: ScannerCommand)
}
