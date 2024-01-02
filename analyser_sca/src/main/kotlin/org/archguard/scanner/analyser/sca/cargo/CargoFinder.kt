package org.archguard.scanner.analyser.sca.cargo

import org.archguard.scanner.analyser.sca.base.Finder
import org.archguard.scanner.analyser.sca.base.Parser
import java.io.File

class CargoFinder : Finder() {
    override val parser: Parser = CargoParser()

    override fun isMatch(it: File): Boolean {
        if (it.isDirectory) return false
        return it.name == "Cargo.toml"
    }


}

class CargoParser : Parser() {

}
