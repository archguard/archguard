package org.archguard.dsl.evolution

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.archguard.dsl.base.Element
import org.archguard.dsl.base.model.ActionType
import org.archguard.dsl.base.model.GraphType
import org.archguard.dsl.base.model.ReactiveAction

@Serializable
class ScanModel(
    val name: String,
    val branch: String,
    val features: MutableList<String>,
    val languages: MutableList<String>,
    val specs: MutableList<String>,
)

class ScanModelDecl(val name: String) : Element {
    var languages: MutableList<String> = mutableListOf()
    var features: MutableList<String> = mutableListOf()
    var specs: MutableList<String> = mutableListOf()
    var branch: String = "master"

    fun branch(branch: String) {
        this.branch = branch
    }

    fun feature(feature: String) {
        this.features += feature
    }

    fun features(vararg features: String) {
        this.features += features.toMutableList()
    }

    fun language(lang: String) {
        this.languages += lang
    }

    fun languages(vararg langs: String) {
        this.languages += langs.toMutableList()
    }

    fun spec(spec: String) {
        this.specs += spec
    }

    fun specs(vararg specs: String) {
        this.specs += specs.toMutableList()
    }

    fun create(): ReactiveAction {
        val scanModel = model()

        return ReactiveAction(
            ActionType.CREATE_SCAN,
            scanModel.javaClass.name,
            GraphType.NULL,
            Json.encodeToString(scanModel)
        )
    }

    private fun model() = ScanModel(
        name,
        branch,
        features,
        languages,
        specs
    )

    override fun toString(): String {
        return Json.encodeToString(this.model())
    }
}
