package org.archguard.scanner.analyser.sca.cargo

import org.archguard.scanner.analyser.sca.base.Finder
import java.io.File

class CargoFinder : Finder() {
    override val parser: CargoParser = CargoParser()

    override fun isMatch(it: File): Boolean {
        if (it.isDirectory) return false
        return it.name == "Cargo.toml"
    }
}
