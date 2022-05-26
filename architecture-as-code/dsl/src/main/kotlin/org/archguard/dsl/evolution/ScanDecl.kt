package org.archguard.dsl.evolution

import kotlinx.serialization.Serializable
import org.archguard.dsl.base.Element

@Serializable
class ScanModel() {

}

class ScanModelDecl(name: String) : Element {
    var languages: MutableList<String> = mutableListOf()
    var specs: MutableList<String> = mutableListOf()

    fun spec(spec: String) {
        this.specs += spec
    }

    fun specs(vararg specs: String) {
        this.specs += specs.toMutableList()
    }

    fun language(lang: String) {
        this.languages += lang
    }

    fun languages(vararg langs: String) {
        this.languages += langs.toMutableList()
    }


}
