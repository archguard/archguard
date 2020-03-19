package com.thoughtworks.archgard.scanner.domain

interface Scanner {
    fun scan(context: ScanContext)
}