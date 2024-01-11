package com.thoughtworks.archguard.smartscanner.repository

import chapi.domain.core.CodeDataStruct

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
    }
}