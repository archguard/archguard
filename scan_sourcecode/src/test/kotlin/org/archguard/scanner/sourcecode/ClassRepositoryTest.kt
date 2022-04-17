package org.archguard.scanner.sourcecode

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeField
import chapi.domain.core.CodeFunction
import chapi.domain.core.CodeImport
import org.archguard.scanner.common.ClassRepository
import org.junit.jupiter.api.Test
import java.util.HashMap
import kotlin.test.assertEquals

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

    @Test
    internal fun renaming_for_react_component() {
        var ds: Array<CodeDataStruct> = arrayOf()
        val codeFunction = CodeFunction(Name = "ModuleDependence", IsReturnHtml = true)
        ds += CodeDataStruct(
            Package = "@.pages.analysis.dependence.ModuleDependence.index",
            NodeName = "default",
            Functions = arrayOf(codeFunction)
        )

        val clzRepo = ClassRepository("1", "typescript", "archguard")
        ds.forEach {
            clzRepo.saveClassItem(it)
        }
        ds.forEach {
            clzRepo.saveClassBody(it)
        }

        val keys: MutableMap<String, String> = HashMap()
        keys["name"] = "@.pages.analysis.dependence.ModuleDependence.ModuleDependence"
        keys["module"] = "root"

        val value = clzRepo.findId("code_class", keys)
        assertEquals(true, value!!.isPresent)

        clzRepo.close()
    }

    @Test
    internal fun dependencies_for_typescript_component() {
        var ds: Array<CodeDataStruct> = arrayOf()
        val codeFunction = CodeFunction(Name = "ModuleDependence", IsReturnHtml = true)
        val imp = CodeImport(Source = "ModuleDependenceArgsForm", UsageName = arrayOf("ModuleDependenceArgsForm"))
        ds += CodeDataStruct(
            Package = "@.pages.analysis.dependence.ModuleDependence.index",
            NodeName = "default",
            Functions = arrayOf(codeFunction),
            Imports = arrayOf(imp)
        )

        val clzRepo = ClassRepository("1", "typescript", "archguard")
        ds.forEach {
            clzRepo.saveClassItem(it)
        }
        ds.forEach {
            clzRepo.saveClassBody(it)
        }

        val keys: MutableMap<String, String> = HashMap()
        keys["name"] = "ModuleDependenceArgsForm.ModuleDependenceArgsForm"
        keys["module"] = "root"

        val value = clzRepo.findId("code_class", keys)
        assertEquals(true, value!!.isPresent)

        clzRepo.close()
    }
}