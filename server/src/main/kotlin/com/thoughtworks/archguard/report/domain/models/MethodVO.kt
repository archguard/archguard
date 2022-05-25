package com.thoughtworks.archguard.report.domain.models

data class MethodVO(val moduleName: String, val packageName: String, val className: String, val methodName: String, val args: String) {
    companion object {
        fun create(fullName: String): MethodVO {

            val args = fullName.substring(fullName.indexOfLast { it == '(' } + 1, fullName.indexOfLast { it == ')' })
            val fullNameWithOutArgs = fullName.substring(0, fullName.indexOfFirst { it == '(' })
            val methodName = fullNameWithOutArgs.substring(fullNameWithOutArgs.indexOfLast { it == '.' } + 1)
            val fullNameWithOutMethodAndArgs = fullNameWithOutArgs.substring(0, fullNameWithOutArgs.indexOfLast { it == '.' })
            val classVO = ClassVO.create(fullNameWithOutMethodAndArgs)
            val moduleName = classVO.moduleName
            val packageName = classVO.packageName
            val className = classVO.className
            return MethodVO(moduleName, packageName, className, methodName, args)
        }
    }
}
