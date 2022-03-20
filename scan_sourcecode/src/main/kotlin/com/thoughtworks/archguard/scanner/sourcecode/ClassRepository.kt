package com.thoughtworks.archguard.scanner.sourcecode

import chapi.domain.core.*
import infrastructure.SourceBatch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.*

private const val DEFAULT_MODULE_NAME = "root"

class ClassRepository(systemId: String) {
    private val batch: SourceBatch = SourceBatch()
    private val systemId: String

    init {
        this.systemId = systemId
    }

    fun saveClassElement(clz: CodeDataStruct) {
        val clzId = saveClass(clz)
        saveClassDependencies(clzId, clz.Imports, clz.Package, clz.NodeName)
        saveClassFields(clzId, clz.Fields, clz.NodeName)
        saveClassCallees(clz.Functions, DEFAULT_MODULE_NAME, clz.NodeName)
        saveClassParent(clzId, DEFAULT_MODULE_NAME, clz.Imports, clz.Extend)
        saveClassMethods(clzId, clz.Functions, clz.NodeName, clz.Package)
    }

    private fun saveClassCallees(
        functions: Array<CodeFunction>,
        moduleName: String,
        clzName: String
    ) {
        for (function in functions) {
            val mId = findMethodIdByClzName(function, clzName, function.Name)?.orElse("") ?: continue
            for (call in function.FunctionCalls) {
                saveMethodCall(mId, call, moduleName, clzName)
            }
        }
    }

    private fun saveMethodCall(callerId: String, callee: CodeCall, moduleName: String, clzName: String) {
        val calleeId: String? = saveOrGetCalleeMethod(callee, moduleName, clzName)
        val callees: MutableMap<String, String> = HashMap()
        callees["id"] = generateId()
        callees["system_id"] = systemId
        callees["a"] = callerId
        callees["b"] = calleeId.orEmpty()
        batch.add("code_ref_method_callees", callees)
    }

    private fun saveOrGetCalleeMethod(callee: CodeCall, module: String, clzName: String): String? {
        val methodId: Optional<String?>? = findMethodId(module, clzName, callee.Parameters, callee.FunctionName)
        return methodId?.orElseGet { saveCalleeMethod(callee) }
    }

    private fun saveCalleeMethod(m: CodeCall): String {
        return doSaveCalleeMethod(m)
    }

    private fun doSaveCalleeMethod(m: CodeCall): String {
        val mId = generateId()
        val time: String = currentTime
        val values: MutableMap<String, String> = HashMap()
        values["id"] = mId
        values["system_id"] = systemId
        values["clzname"] = m.NodeName
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
        values["loc"] = (m.Position.StopLine - m.Position.StartLine).toString()
        batch.add("code_method", values)
        return mId
    }


    private fun findMethodIdByClzName(m: CodeFunction, clzName: String, funcName: String): Optional<String?>? {
        return findMethodId(DEFAULT_MODULE_NAME, clzName, m.Parameters, funcName)
    }

    private fun findMethodId(
        moduleName: String,
        clzName: String,
        parameters: Array<CodeProperty>,
        callNodeName: String
    ): Optional<String?>? {
        val keys: MutableMap<String, String> = HashMap()
        keys["clzname"] = clzName
        keys["name"] = callNodeName
        keys["module"] = moduleName
        keys["argumenttypes"] = Json.encodeToString(parameters)
        return batch.findId("code_method", keys)
    }


    private fun saveClassParent(
        clzId: String,
        module: String,
        imports: Array<CodeImport>,
        extend: String
    ) {
        val imp = imports.filter { it.Source.split(".").last() == extend }
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

    private fun saveClassDependencies(clzId: String, imports: Array<CodeImport>, packageName: String, clzName: String) {
        for (clz in imports) {
            val name = clz.Source
            val clzDependenceId = saveOrGetDependentClass(name, DEFAULT_MODULE_NAME)
            doSaveClassDependence(clzId, clzDependenceId, "${packageName}.${clzName}", name)
        }
    }

    private fun saveOrGetDependentClass(name: String, moduleName: String = DEFAULT_MODULE_NAME): String? {
        val idOpt = findClass(name, moduleName, null)
        val index: Int = name.lastIndexOf(".")
        val packageName: String? = if (index < 0) null else name.substring(0, index)
        val className: String = if (index < 0) name else name.substring(index + 1)
        val clzDependenceId = idOpt!!.orElseGet {
            doSaveClass(
                name,
                moduleName,
                "",
                thirdparty = true,
                isTest = false,
                packageName = packageName,
                className = className
            )
        }
        return clzDependenceId
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

    private fun doSaveClass(
        name: String, module: String, access: String, thirdparty: Boolean, isTest: Boolean,
        packageName: String?, className: String
    ): String {
        val time: String = currentTime
        val clzId = generateId()
        val values: MutableMap<String, String> = HashMap()
        values["id"] = clzId
        values["system_id"] = systemId
        values["name"] = name
        values["is_thirdparty"] = if (thirdparty) "true" else "false"
        values["is_test"] = if (isTest) "true" else "false"
        values["updatedAt"] = time
        values["createdAt"] = time
        values["module"] = module
        values["package_name"] = packageName.orEmpty()
        values["class_name"] = className
        values["access"] = access
        batch.add("code_class", values)
        return clzId
    }

    private fun findClass(name: String, module: String?, access: String?): Optional<String?>? {
        val keys: MutableMap<String, String> = HashMap()
        keys["name"] = name
        if (module != null) {
            keys["module"] = module
        }
        if (access != null) {
            keys["access"] = access
        }

        return batch.findId("code_class", keys)
    }

    private fun saveClassMethods(clzId: String, functions: Array<CodeFunction>, clzName: String, pkgName: String) {
        for (method in functions) {
            val methodId = doSaveMethod(clzName, method, pkgName)
            doSaveClassMethodRelations(clzId, methodId)

            for (localVariable in method.LocalVariables) {
                saveMethodField(methodId, localVariable, clzName)
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
        val methodFields: MutableMap<String, String> = HashMap()
        methodFields["id"] = generateId()
        methodFields["system_id"] = systemId
        methodFields["a"] = methodId
        methodFields["b"] = fieldId
        batch.add("code_ref_class_fields", methodFields)
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
        values["key"] = map.Key
        var value = map.Value
        if (value.contains("'")) {
            value = value.replace("'", "''")
        }
        values["value"] = value

        batch.add("code_annotation_value", values)
    }

    private fun doSaveMethod(clzName: String, m: CodeFunction, pkgName: String): String {
        val mId = generateId()
        val time: String = currentTime
        val values: MutableMap<String, String> = HashMap()
        values["id"] = mId
        values["system_id"] = systemId
        values["clzname"] = clzName
        values["name"] = m.Name
        values["returntype"] = m.ReturnType
        values["argumenttypes"] = m.Parameters.map { it.TypeType }.joinToString(",")

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
        values["is_test"] = if (m.isJUnitTest()) "true" else "false"
        values["loc"] = (m.Position.StopLine - m.Position.StartLine).toString()
        batch.add("code_method", values)
        return mId
    }

    private fun saveClassFields(clzId: String, fields: Array<CodeField>, clzName: String) {
        for (field in fields) {
            val id = generateId()
            val time: String = currentTime
            val values: MutableMap<String, String> = HashMap()

            // for TypeScript
            var name = field.TypeValue
            if (field.TypeValue.contains("'")) {
                name = field.TypeValue.replace("'", "''")
            }
            if (field.TypeValue.contains("\n")) {
                println("contains \n will give up: --------------\n-------------")
                name = ""
            }

            values["id"] = id
            values["system_id"] = systemId
            values["name"] = name
            values["clzname"] = clzName
            values["type"] = field.TypeType
            values["updatedAt"] = time
            values["createdAt"] = time
            batch.add("code_field", values)

            val relation: MutableMap<String, String> = HashMap()
            relation["id"] = generateId()
            relation["system_id"] = systemId
            relation["a"] = clzId
            relation["b"] = id
            batch.add("code_ref_class_fields", relation)
        }
    }

    private fun saveClass(clz: CodeDataStruct): String {
        val time = currentTime
        val clzId = generateId()
        val values: MutableMap<String, String> = HashMap()
        values["id"] = clzId
        values["system_id"] = systemId
        values["name"] = clz.Package + "." + clz.NodeName
        values["is_thirdparty"] = "false"
        values["is_test"] = "false"
        values["updatedAt"] = time
        values["createdAt"] = time
        values["module"] = DEFAULT_MODULE_NAME
        values["package_name"] = clz.Package
        values["class_name"] = clz.NodeName
        values["access"] = "todo"
        batch.add("code_class", values)

        return clzId
    }

    companion object {
        fun generateId(): String {
            return UUID.randomUUID().toString()
        }

        val currentTime: String
            get() {
                val dt = Date()
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                return sdf.format(dt)
            }
    }

    fun close() {
        batch.execute()
        batch.close()
    }
}