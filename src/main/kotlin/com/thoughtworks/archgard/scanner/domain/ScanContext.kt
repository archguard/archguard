package com.thoughtworks.archgard.scanner.domain

<<<<<<< HEAD
class ScanContext {
    var repo: String = ""
    var workspace: String = ""
    var sourcePath: String = ""
=======
import java.io.File

class ScanContext {
    val projectRoot: File = TODO("initialize me")
>>>>>>> feat: add badsmell
}