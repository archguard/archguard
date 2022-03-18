package com.thoughtworks.archguard.scanner.sourcecode

import chapi.domain.core.*
import infrastructure.SourceBatch
import java.text.SimpleDateFormat
import java.util.*

class ClassRepository(systemId: String) {
    private val batch: SourceBatch = SourceBatch()
    private val systemId: String

    init {
        this.systemId = systemId
    }

    fun saveClassElement(clz: CodeDataStruct) {
        val clzId = saveClass(clz)
        saveClassDependencies(clzId, clz.NodeName, clz.Imports)
        saveClassFields(clzId, clz.Fields, clz.NodeName)
        saveClassMethods(clzId, clz.Functions, clz.NodeName, clz.Package)
    }

    private fun saveClassDependencies(clzId: String, nodeName: String, imports: Array<CodeImport>) {
        for (clz in imports) {
//            val clzDependenceId: String = saveOrGetDependentClass(clz, module, moduleDependencies)/**/
            //save _ClassDependence

            //third-party
            val name = clz.Source
            val idOpt = findClass(name, null, null)
            val index: Int = name.lastIndexOf(".")
            val packageName: String? = if (index < 0) null else name.substring(0, index)
            val className: String = if (index < 0) name else name.substring(index + 1)
            val clzDependenceId = idOpt!!.orElseGet {
                doSaveClass(
                    name,
                    "",
                    "",
                    thirdparty = true,
                    isTest = false,
                    packageName = packageName,
                    className = className
                )
            }

            doSaveClassDependence(clzId, clzDependenceId)
        }
    }

    private fun doSaveClassDependence(clzId: String, clzDependenceId: String?) {
        val values: MutableMap<String, String> = HashMap()
        values["id"] = generateId()
        values["system_id"] = systemId
        values["a"] = clzId
        values["b"] = clzDependenceId.orEmpty()
        batch.add("_ClassDependences", values)
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
        batch.add("JClass", values)
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

        return batch.findId("JClass", keys)
    }

    private fun saveClassMethods(clzId: String, functions: Array<CodeFunction>, clzName: String, pkgName: String) {
        for (method in functions) {
            val methodId = doSaveMethod(clzName, method, pkgName)

            method.Annotations.forEach {
                doSaveAnnotation(it, methodId)
            }
        }
    }

    private fun doSaveAnnotation(annotation: CodeAnnotation, methodId: String) {
        val id = generateId()
        val values: MutableMap<String, String> = HashMap()
        values["id"] = id
        values["system_id"] = systemId
        values["targetType"] = "todo"
        values["targetId"] = methodId
        values["name"] = annotation.Name
        batch.add("JAnnotation", values)


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
        values["value"] = map.Value
        batch.add("JAnnotationValue", values)
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

        if(m.Modifiers.isNotEmpty()){
            values["access"] = m.Modifiers[0]
        } else {
            values["access"] = "public"
        }

        values["module"] = pkgName
        values["package_name"] = pkgName
        values["class_name"] = clzName
        values["updatedAt"] = time
        values["createdAt"] = time
        values["is_test"] = if (m.isJUnitTest()) "true" else "false"
        batch.add("JMethod", values)
        return mId
    }

    private fun saveClassFields(clzId: String, fields: Array<CodeField>, clzName: String) {
        for (field in fields) {
            val id = generateId()
            val time: String = currentTime
            val values: MutableMap<String, String> = HashMap()

            // for TypeScript
            var name = field.TypeValue
            if(field.TypeValue.contains("'")) {
                name = field.TypeValue.replace("'", "''")
            }

            values["id"] = id
            values["system_id"] = systemId
            values["name"] = name
            values["clzname"] = clzName
            values["type"] = field.TypeType
            values["updatedAt"] = time
            values["createdAt"] = time
            batch.add("JField", values)

            val relation: MutableMap<String, String> = HashMap()
            relation["id"] = generateId()
            relation["system_id"] = systemId
            relation["a"] = clzId
            relation["b"] = id
            batch.add("_ClassFields", relation)
        }
    }

    private fun saveClass(clz: CodeDataStruct): String {
        val time = currentTime
        val clzId = generateId()
        val values: MutableMap<String, String> = HashMap()
        values["id"] = clzId
        values["system_id"] = systemId
        values["name"] = clz.NodeName
        values["is_thirdparty"] = "false"
        values["is_test"] = "false"
        values["updatedAt"] = time
        values["createdAt"] = time
        values["module"] = clz.Package
        values["package_name"] = clz.Package
        values["class_name"] = clz.NodeName
        values["access"] = "todo"
        batch.add("JClass", values)

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