package com.thoughtworks.archguard.smartscanner.repository

import chapi.domain.core.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.thoughtworks.archguard.infrastructure.SourceBatch
import com.thoughtworks.archguard.smartscanner.repository.RepositoryHelper.generateId
import org.jetbrains.annotations.TestOnly
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

private const val DEFAULT_MODULE_NAME = "root"
private const val THIRD_PARTY = "root"

class BatchClassRepository(systemId: String, language: String, workspace: String) {
    private val batch: SourceBatch = SourceBatch()
    private val count = AtomicInteger(0)
    private val batchStep = 100
    private val systemId: String
    private val language: String
    private val workspace: String

    init {
        this.systemId = systemId
        this.language = language
        this.workspace = workspace
    }

    fun saveClassItem(clz: CodeDataStruct) {
        val clzId = saveClass(clz)
        saveClassFields(clzId, clz.Fields, clz.NodeName)
        saveClassMethods(clzId, clz.Functions, clz.NodeName, clz.Package, clz.FilePath)

        count.incrementAndGet()
        if (count.get() == batchStep) {
            flush()
            count.compareAndSet(batchStep, 0)
        }
    }

    fun saveClassBody(clz: CodeDataStruct) {
        val clzId = saveOrGetClzId(clz)!!
        saveClassDependencies(clzId, clz.Imports, clz.Package, clz.NodeName, clz.FilePath, clz.Functions, clz.Exports)
        saveClassCallees(clz.Functions, DEFAULT_MODULE_NAME, clz.NodeName, clz.Package)
        saveClassParent(clzId, DEFAULT_MODULE_NAME, clz.Imports, clz.Extend)
        saveClassAnnotation(clzId, clz.Annotations)

        count.incrementAndGet()
        if (count.get() == batchStep) {
            flush()
            count.compareAndSet(batchStep, 0)
        }
    }

    private fun saveOrGetClzId(clz: CodeDataStruct): String? {
        var clzFullName = "${clz.Package}.${clz.NodeName}"

        if (isJs()) {
            val component = ReactComponentClass.from(clz)
            clzFullName = "${component.packageName}.${component.className}"
        }

        val idOrOpt = findClass(clzFullName, DEFAULT_MODULE_NAME)

        return idOrOpt.orElseGet {
            saveClass(clz)
        }
    }

    private fun saveClassAnnotation(clzId: String, annotations: List<CodeAnnotation>) {
        annotations.forEach {
            doSaveAnnotation(it, clzId)
        }
    }

    private fun saveClassCallees(functions: List<CodeFunction>, moduleName: String, clzName: String, pkgName: String) {
        for (function in functions) {
            val mId = findMethodIdByClzName(function, clzName, function.Name, pkgName)?.orElse("") ?: continue
            for (call in function.FunctionCalls) {
                // not a correct NodeName
                if (call.NodeName.contains("\"") || call.NodeName.contains("'")) {
                    continue
                }

                saveMethodCall(mId, call, moduleName, clzName, call.Package)
            }
        }
    }

    private fun saveMethodCall(
        callerId: String,
        callee: CodeCall,
        moduleName: String,
        clzName: String,
        pkgName: String
    ) {
        val calleeId: String? = saveOrGetCalleeMethod(callee, moduleName, clzName, pkgName)
        val callees: MutableMap<String, String> = HashMap()
        callees["id"] = generateId()
        callees["system_id"] = systemId
        callees["a"] = callerId
        callees["b"] = calleeId.orEmpty()
        batch.add("code_ref_method_callees", callees)
    }

    private fun saveOrGetCalleeMethod(callee: CodeCall, module: String, clzName: String, pkgName: String): String? {
        val methodId: Optional<String?>? =
            findCalleeMethodId(module, clzName, callee.Parameters, callee.FunctionName, pkgName)
        return methodId?.orElseGet { saveCalleeMethod(callee) }
    }

    private fun saveCalleeMethod(m: CodeCall): String {
        return doSaveCalleeMethod(m)
    }

    private fun doSaveCalleeMethod(m: CodeCall): String {
        val mId = generateId()
        val time: String = RepositoryHelper.getCurrentTime()
        val values: MutableMap<String, String> = HashMap()
        values["id"] = mId
        values["system_id"] = systemId
        values["clzname"] = "${m.Package}.${m.NodeName}"
        values["name"] = m.FunctionName
        values["returntype"] = ""
        values["argumenttypes"] = m.Parameters.map { it.TypeType }.joinToString(",")
        values["access"] = "public"
        values["module"] = DEFAULT_MODULE_NAME
        values["package_name"] = m.Package
        values["class_name"] = m.NodeName
        values["updatedAt"] = time
        values["createdAt"] = time
        values["is_test"] = "false"
        values["loc"] = "0"
        batch.add("code_method", values)
        return mId
    }

    private fun findMethodIdByClzName(
        m: CodeFunction,
        clzName: String,
        funcName: String,
        pkgName: String
    ): Optional<String?>? {
        return findMethodId(DEFAULT_MODULE_NAME, clzName, m.Parameters, funcName, pkgName)
    }

    private fun findCalleeMethodId(
        module: String,
        clzName: String,
        parameters: List<CodeProperty>,
        functionName: String,
        pkgName: String,
    ): Optional<String?>? {
        if (module.isNotEmpty()) {
            return findMethodId(module, clzName, parameters, functionName, pkgName)
        }

        return findMethodId(THIRD_PARTY, clzName, parameters, functionName, pkgName)
    }

    private fun findMethodId(
        moduleName: String,
        clzName: String,
        parameters: List<CodeProperty>,
        callNodeName: String,
        pkgName: String
    ): Optional<String?>? {
        val keys: MutableMap<String, String> = HashMap()
        keys["clzname"] = "$pkgName.$clzName"
        keys["name"] = callNodeName
        keys["module"] = moduleName
        keys["argumenttypes"] = jacksonObjectMapper().writeValueAsString(parameters)
        return batch.findId("code_method", keys)
    }

    private fun saveClassParent(clzId: String, module: String, imports: List<CodeImport>, extend: String) {
        var delimiters = "."
        if (isJs()) {
            delimiters = "/"
        }

        val imp = imports.filter { it.Source.split(delimiters).last() == extend }
        var moduleName = module
        if (imp.isNotEmpty()) {
            moduleName = imp[0].Source
        }

        val parentId = saveOrGetDependentClass(extend, moduleName)
        if (parentId != null) {
            doSaveClassParent(clzId, parentId)
        }
    }

    private fun doSaveClassParent(clzId: String, parentClzId: String) {
        val values: MutableMap<String, String> = HashMap()
        values["id"] = generateId()
        values["system_id"] = systemId
        values["a"] = clzId
        values["b"] = parentClzId
        batch.add("code_ref_class_parent", values)
    }

    private fun saveClassDependencies(
        clzId: String,
        imports: List<CodeImport>,
        packageName: String,
        clzName: String,
        filePath: String,
        clzFunctions: List<CodeFunction>,
        exports: List<CodeExport>
    ) {
        for (import in imports) {
            if (isJs()) {
                import.UsageName.forEach {
                    var sourceName = packageName

                    var targetName = import.Source
                    targetName = ReactComponentClass.convertTypeScriptImport(targetName, filePath)

                    val mayBeComponent = packageName.endsWith(".index") && clzName == "default"
                    if (mayBeComponent) {
                        val functions = clzFunctions.filter { func -> func.IsReturnHtml }
                        val isComponent = functions.isNotEmpty()
                        if (isComponent) {
                            sourceName = packageName.removeSuffix(".index")

                            if (exports.isNotEmpty()) {
                                sourceName = sourceName + "." + exports[0].Name
                            } else {
                                sourceName = sourceName + "." + functions[0].Name
                            }
                        }
                    }

                    targetName = "$targetName.$it"
                    val clzDependenceId = saveOrGetDependentClass(targetName, DEFAULT_MODULE_NAME)
                    doSaveClassDependence(clzId, clzDependenceId, sourceName, targetName)
                }
            } else {
                val sourceName = "$packageName.$clzName"

                val importSource = import.Source
                val clzDependenceId = saveOrGetDependentClass(importSource, DEFAULT_MODULE_NAME)
                doSaveClassDependence(clzId, clzDependenceId, sourceName, importSource)
            }
        }
    }

    private fun isJs() = language == "typescript" || language == "javascript"

    private fun saveOrGetDependentClass(name: String, moduleName: String = DEFAULT_MODULE_NAME): String? {
        // own module
        var idOpt = findClass(name, moduleName)
        if (idOpt.isPresent) {
            return idOpt.get()
        }

        // third-party
        idOpt = findClass(name, THIRD_PARTY)
        if (idOpt.isPresent) {
            return idOpt.get()
        }

        val index: Int = name.lastIndexOf(".")
        val packageName: String? = if (index < 0) null else name.substring(0, index)
        val className: String = if (index < 0) name else name.substring(index + 1)

        if (name.isEmpty() && className.isEmpty() && packageName.isNullOrEmpty()) {
            return null
        }

        // module name empty for third-part deps
        return saveDepClass(
            name,
            THIRD_PARTY,
            "public",
            thirdParty = true,
            isTest = false,
            packageName = packageName,
            className = className
        )
    }

    private fun doSaveClassDependence(clzId: String, clzDependenceId: String?, sourceName: String, name: String) {
        val values: MutableMap<String, String> = HashMap()
        values["id"] = generateId()
        values["system_id"] = systemId
        values["a"] = clzId
        values["b"] = clzDependenceId.orEmpty()
        values["source"] = sourceName
        values["target"] = name
        batch.add("code_ref_class_dependencies", values)
    }

    /**
     * find class id by name and module
     */
    fun findClass(name: String, module: String?): Optional<String?> {
        val keys: MutableMap<String, String> = HashMap()
        keys["name"] = name
        if (module != null) {
            keys["module"] = module
        }

        return batch.findId("code_class", keys)
    }

    private fun saveClassMethods(
        clzId: String,
        functions: List<CodeFunction>,
        clzName: String,
        pkgName: String,
        filePath: String
    ) {
        for (method in functions) {
            if (clzName.isEmpty() && pkgName.isEmpty()) {
                continue
            }

            val methodId = doSaveMethod(clzName, method, pkgName, filePath)
            doSaveClassMethodRelations(clzId, methodId)

            // would not save local vars for JavaScript/TypeScript
            if (!isJs()) {
                for (localVariable in method.LocalVariables) {
                    saveMethodField(methodId, localVariable, clzName)
                }
            }

            method.Annotations.forEach {
                doSaveAnnotation(it, methodId)
            }
        }
    }

    private fun saveMethodField(methodId: String, localVariable: CodeProperty, clzName: String) {
        val fieldIdOpt = findFieldId(localVariable, clzName)
        if (!fieldIdOpt!!.isPresent) {
            return
        }

        val fieldId = fieldIdOpt.get()
        saveCodeRefClassFields(methodId, fieldId)
    }

    private fun findFieldId(field: CodeProperty, clzName: String): Optional<String?>? {
        val keys: MutableMap<String, String> = HashMap()
        keys["clzname"] = clzName
        keys["name"] = field.TypeValue
        return batch.findId("code_field", keys)
    }

    private fun doSaveClassMethodRelations(clzId: String, mId: String) {
        val values: MutableMap<String, String> = HashMap()
        values["id"] = generateId()
        values["system_id"] = systemId
        values["a"] = clzId
        values["b"] = mId
        batch.add("code_ref_class_methods", values)
    }

    private fun doSaveAnnotation(annotation: CodeAnnotation, methodId: String) {
        val id = generateId()
        val values: MutableMap<String, String> = HashMap()
        values["id"] = id
        values["system_id"] = systemId
        values["targetType"] = "todo"
        values["targetId"] = methodId
        values["name"] = annotation.Name

        batch.add("code_annotation", values)

        for (keyValue in annotation.KeyValues) {
            doSaveAnnotationValue(id, keyValue)
        }
    }

    private fun doSaveAnnotationValue(annotationId: String, map: AnnotationKeyValue) {
        val id = generateId()
        val values: MutableMap<String, String> = HashMap()
        values["id"] = id
        values["system_id"] = systemId
        values["annotationId"] = annotationId
        values["key"] = escapeSingleQuotes(map.Key)
        values["value"] = escapeSingleQuotes(map.Value)

        batch.add("code_annotation_value", values)
    }

    private fun doSaveMethod(clzName: String, m: CodeFunction, pkgName: String, filePath: String): String {
        val mId = generateId()
        val time: String = RepositoryHelper.getCurrentTime()
        val values: MutableMap<String, String> = HashMap()
        values["id"] = mId
        values["system_id"] = systemId
        values["clzname"] = "$pkgName.$clzName"
        values["name"] = m.Name
        values["returntype"] = m.ReturnType
        values["argumenttypes"] = escapeSingleQuotes(m.Parameters.joinToString(",") { it.TypeType })

        if (m.Modifiers.isNotEmpty()) {
            values["access"] = m.Modifiers[0]
        } else {
            values["access"] = "public"
        }

        values["module"] = DEFAULT_MODULE_NAME
        values["package_name"] = pkgName
        values["class_name"] = clzName
        values["updatedAt"] = time
        values["createdAt"] = time
        values["is_test"] = ReactComponentClass.isTest(m, filePath)
        values["loc"] = (m.Position.StopLine - m.Position.StartLine).toString()
        batch.add("code_method", values)
        return mId
    }

    private val FIELD_NAME = "[a-zA-Z_\$@]+".toRegex()

    private fun saveClassFields(clzId: String, fields: List<CodeField>, clzName: String) {
        for (field in fields) {
            val id = generateId()
            val time: String = RepositoryHelper.getCurrentTime()
            val values: MutableMap<String, String> = HashMap()

            val name = field.TypeKey
            // for TypeScript
            if (isJs()) {
                if (!FIELD_NAME.matches(name)) {
                    continue
                }
            }

            values["id"] = id
            values["system_id"] = systemId
            values["name"] = name
            values["clzname"] = clzName
            values["type"] = escapeSingleQuotes(field.TypeType)

            values["updatedAt"] = time
            values["createdAt"] = time
            batch.add("code_field", values)

            // JavaScript or TypeScript can use global field
            if (isJs()) {
                val keys: MutableMap<String, String> = HashMap()
                keys["a"] = clzId
                keys["b"] = id

                val optional = batch.findId("code_ref_class_fields", keys)
                if (optional == null) {
                    saveCodeRefClassFields(clzId, id)
                }
            } else {
                saveCodeRefClassFields(clzId, id)
            }
        }
    }

    private fun escapeSingleQuotes(value: String): String {
        if (value.contains("'")) {
            return value.replace("'", "''")
        }
        return value
    }

    private fun saveCodeRefClassFields(clzId: String, id: String) {
        val relation: MutableMap<String, String> = HashMap()
        relation["id"] = generateId()
        relation["system_id"] = systemId
        relation["a"] = clzId
        relation["b"] = id
        batch.add("code_ref_class_fields", relation)
    }

    data class CodeClass(
        val id: String,
        val systemId: String,
        val name: String,
        val loc: String,
        val isThirdParty: Boolean,
        val isTest: Boolean,
        val updatedAt: String,
        val createdAt: String,
        val module: String,
        val packageName: String,
        val className: String,
        val access: String
    )

    fun createCodeClass(codeClass: CodeClass) {
        val values = linkedMapOf(
            "id" to codeClass.id,
            "system_id" to codeClass.systemId,
            "name" to codeClass.name,
            "loc" to codeClass.loc,
            "is_thirdparty" to codeClass.isThirdParty.toString(),
            "is_test" to codeClass.isTest.toString(),
            "updatedAt" to codeClass.updatedAt,
            "createdAt" to codeClass.createdAt,
            "module" to codeClass.module,
            "package_name" to codeClass.packageName,
            "class_name" to codeClass.className,
            "access" to codeClass.access
        )

        batch.add("code_class", values)
    }

    fun saveDepClass(
        name: String,
        moduleName: String,
        accessName: String,
        thirdParty: Boolean,
        isTest: Boolean,
        packageName: String?,
        className: String
    ): String {
        val time = RepositoryHelper.getCurrentTime()
        val clzId = generateId()

        val codeClass = CodeClass(
            id = clzId,
            systemId = systemId,
            name = name,
            loc = "0",
            isThirdParty = thirdParty,
            isTest = isTest,
            updatedAt = time,
            createdAt = time,
            module = moduleName,
            packageName = packageName.orEmpty(),
            className = className,
            access = accessName
        )

        createCodeClass(codeClass)
        return clzId
    }

    fun saveClass(clz: CodeDataStruct): String {
        val time = RepositoryHelper.getCurrentTime()
        val clzId = generateId()

        var pkgName = handleForPackageName(clz.FilePath, clz.Package)
        var clzName = clz.NodeName
        var fullName = "$pkgName.$clzName"

        if (isJs()) {
            val component = ReactComponentClass.from(clz)
            fullName = "${component.packageName}.${component.className}"
            pkgName = component.packageName
            clzName = component.className
        }

        val codeClass = CodeClass(
            id = clzId,
            systemId = systemId,
            name = fullName,
            loc = (clz.Position.StopLine - clz.Position.StartLine).toString(),
            isThirdParty = false,
            isTest = false,
            updatedAt = time,
            createdAt = time,
            module = DEFAULT_MODULE_NAME,
            packageName = pkgName,
            className = clzName,
            access = "todo"
        )

        createCodeClass(codeClass)
        return clzId
    }

    fun findId(table: String, keys: Map<String, String>): Optional<String>? {
        return batch.findId(table, keys)
    }

    fun flush() {
        batch.execute()
    }

    @TestOnly
    fun getStore(table: String): MutableList<MutableMap<String, String>>? {
        return batch.getStore(table)
    }

    fun close() {
        batch.execute()
        batch.close()
    }

    companion object {
        /**
         * This method is used to handle the package name for a given path.
         *
         * @see File.separator
         */
        fun handleForPackageName(path: String, packageName: String) = packageName.ifEmpty {
            val filePath = path
                .substringAfterLast(File.separator)
                .substringBeforeLast(".")
            // xxx.c -> xxx
            if (path.endsWith(".c") || path.endsWith(".h") || path.endsWith(".cpp")) {
                filePath
            } else {
                packageName
            }
        }
    }
}
