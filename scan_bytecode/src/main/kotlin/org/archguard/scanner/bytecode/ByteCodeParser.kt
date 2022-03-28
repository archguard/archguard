package org.archguard.scanner.bytecode

import chapi.domain.core.AnnotationKeyValue
import chapi.domain.core.CodeAnnotation
import chapi.domain.core.CodeDataStruct
import chapi.domain.core.CodeFunction
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Type
import org.objectweb.asm.tree.AnnotationNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class ByteCodeParser {
    private val logger = LoggerFactory.getLogger(ByteCodeParser::class.java)

    @Throws(Exception::class, IOException::class)
    fun parseClassFile(file: File): CodeDataStruct {
        logger.debug("ClassParser visitClass: {}", file)
        val classNode = getDataStructure(file)
        return createClass(classNode)
    }

    @Throws(IOException::class)
    private fun getDataStructure(file: File): ClassNode {
        val fileInputStream = FileInputStream(file)
        val classNode = ClassNode()
        val cr = ClassReader(fileInputStream)
        cr.accept(classNode, 0)
        fileInputStream.close()

        return classNode
    }

    private fun createClass(classNode: ClassNode): CodeDataStruct {
        val ds = CodeDataStruct()
        ds.NodeName = getDataStructureName(classNode.name).toString()

        classNode.methods.forEach {
            ds.Functions += this.createMethod(it)
        }

        ds.Extend = getDataStructureName(classNode.superName)
        ds.Implements = classNode.interfaces?.map {
            getDataStructureName(it)
        }?.toTypedArray() ?: arrayOf()

        ds.Annotations = classNode.visibleAnnotations?.map {
            createAnnotation(it)
        }?.toTypedArray() ?: arrayOf()

        return ds
    }

    private fun createAnnotation(annotation: AnnotationNode): CodeAnnotation {
        val name: String = Type.getType(annotation.desc).className
        val codeAnnotation = CodeAnnotation(Name = name)

        if (annotation.values != null) {
            val values: List<Any> = annotation.values
            var i = 0
            while (i < values.size) {
                codeAnnotation.KeyValues += AnnotationKeyValue(values[i].toString(), values[i + 1].toString())
                i += 2
            }
        }

        return codeAnnotation
    }

    private fun createMethod(methodNode: MethodNode): CodeFunction {
        val codeFunction = CodeFunction(Name = methodNode.name)
        return codeFunction
    }


    private fun getDataStructureName(internalName: String): String {
        return Type.getObjectType(internalName).className
    }
}