package org.archguard.scanner.context

interface ContainerService

interface Context {
    val language: ScannerSpec
    val features: List<ScannerSpec>

    // TODO hold the scanners and callback apis
    val callbackUrl: String
    fun saveApi(api: List<ContainerService>) {
        TODO("save the api schema via callback url")
    }
}
