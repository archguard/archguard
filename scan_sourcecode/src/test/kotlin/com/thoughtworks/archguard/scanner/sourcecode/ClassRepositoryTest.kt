package com.thoughtworks.archguard.scanner.sourcecode

import chapi.domain.core.CodeDataStruct
import org.junit.jupiter.api.Test
import java.util.HashMap
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

internal class ClassRepositoryTest {
    @Test
    internal fun should_handle_typescript_naming_issue() {
        //  {"NodeName":"default","Package":"@.pages.analysis.dependence.ModuleDependence.index","FilePath":"src/pages/analysis/dependence/ModuleDependence/index.tsx","Fields":[],"MultipleExtend":[],"Implements":[],"Functions":[{"Name":"ModuleDependence","FilePath":"src/pages/analysis/dependence/ModuleDependence/index.tsx","MultipleReturns":[],"Parameters":[{"Modifiers":[],"TypeValue":"{location}","TypeType":"any","Annotations":[],"ObjectValue":[],"ReturnTypes":[],"Parameters":[]}],"FunctionCalls":[{"Parameters":[{"Modifiers":[],"TypeValue":"","TypeType":"object","Annotations":[],"ObjectValue":[],"ReturnTypes":[],"Parameters":[]}],"Position":{}}],"Annotations":[],"Modifiers":[],"InnerStructures":[],"InnerFunctions":[],"Position":{"StartLine":7,"StartLinePosition":7,"StopLine":15},"LocalVariables":[],"IsReturnHtml":true}],"InnerStructures":[],"Annotations":[],"FunctionCalls":[],"Parameters":[],"Imports":[{"Source":"react","UsageName":["React"]},{"Source":"./components/ModuleConfig","UsageName":["ModuleConfig"]},{"Source":"./components/ModuleDependenceGraph","UsageName":["ModuleDependenceGraph"]},{"Source":"./components/ModuleDependenceTable","UsageName":["ModuleDependenceTable"]},{"Source":"./index.less","UsageName":[]}],"Exports":[]}
        var ds: Array<CodeDataStruct> = arrayOf()
        ds += CodeDataStruct(
            Package = "@.pages.analysis.dependence.ModuleDependence.index",
            NodeName = "default"

            )

        val clzRepo = ClassRepository("1", "typescript", "archguard")
        ds.forEach {
            clzRepo.saveClassItem(it)
        }
        ds.forEach {
            clzRepo.saveClassBody(it)
        }

        val keys: MutableMap<String, String> = HashMap()
        keys["name"] = "@.pages.analysis.dependence.ModuleDependence.index.default"
        keys["module"] = "root"

        val value = clzRepo.findId("code_class", keys)
        assertEquals(true, value!!.isPresent)

        clzRepo.close()
    }
}