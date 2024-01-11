package com.thoughtworks.archguard.smartscanner.repository

import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import com.thoughtworks.archguard.smartscanner.infra.importConvert
import java.io.File

class ReactComponentClass(val packageName: String, val className: String) {
    companion object {
        fun isComponent(filePath: String) = filePath.endsWith(".tsx") || filePath.endsWith(".jsx")

        fun from(clz: CodeDataStruct): ReactComponentClass {
            var pkgName = clz.Package
            var clzName = clz.NodeName
            var isProcessedComponent = false

            // for `Component/index.tsx`
            val mayBeAComponent = pkgName.endsWith(".index") && clzName == "default"
            if (mayBeAComponent) {
                val functions = clz.Functions.filter { it.IsReturnHtml }
                val isAComponent = functions.isNotEmpty()
                if (isAComponent) {
                    isProcessedComponent = true

                    pkgName = pkgName.removeSuffix(".index")
                    clzName = functions[0].Name
                }
            }

            // for `Component/SomeComponent.tsx`
            val filePath = clz.FilePath
            if (!isProcessedComponent && clzName == "default" && ReactComponentClass.isComponent(filePath)) {
                val functions = clz.Functions.filter { it.IsReturnHtml }
                val isAComponent = functions.isNotEmpty()
                if (isAComponent) {
                    pkgName = pkgName.removeSuffix(".index")
                    clzName = functions[0].Name
                }
            }

            return ReactComponentClass(pkgName, clzName)
        }

        fun isTest(function: CodeFunction, filePath: String): String {
            val testPath = arrayOf("src", "test").joinToString(File.separator)
            if (filePath.contains(testPath)) {
                return "true"
            }

            return if (function.isJUnitTest()) "true" else "false"
        }

        fun convertTypeScriptImport(importSource: String, filePath: String): String {
            var imp = importSource
            if (!imp.startsWith("@")) {
                imp = importConvert(filePath, imp)
                if (imp.startsWith("src/")) {
                    imp = imp.replaceFirst("src/", "@/")
                }
            }

            imp = imp.replace("/", ".")
            return imp
        }
    }
}