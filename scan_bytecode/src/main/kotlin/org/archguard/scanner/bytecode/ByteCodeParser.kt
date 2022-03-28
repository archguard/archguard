package org.archguard.scanner.bytecode

import chapi.domain.core.*
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
    private val FIELD_ALLOWED = CodeConstants.ACC_PUBLIC or CodeConstants.ACC_PROTECTED or CodeConstants.ACC_PRIVATE or CodeConstants.ACC_STATIC or
                CodeConstants.ACC_FINAL or CodeConstants.ACC_TRANSIENT or CodeConstants.ACC_VOLATILE
    private val FIELD_EXCLUDED = CodeConstants.ACC_PUBLIC or CodeConstants.ACC_STATIC or CodeConstants.ACC_FINAL

    private var MODIFIERS: MutableMap<Int, String> = mutableMapOf(
        CodeConstants.ACC_PUBLIC to "public",
        CodeConstants.ACC_PROTECTED to "protected",
        CodeConstants.ACC_PRIVATE to "private",
        CodeConstants.ACC_ABSTRACT to "abstract",
        CodeConstants.ACC_STATIC to "static",
        CodeConstants.ACC_FINAL to "final",
        CodeConstants.ACC_STRICT to "strictfp",
        CodeConstants.ACC_TRANSIENT to "transient",
        CodeConstants.ACC_VOLATILE to "volatile",
        CodeConstants.ACC_SYNCHRONIZED to "synchronized",
        CodeConstants.ACC_NATIVE to "native",
    )


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

    private fun appendModifiers(classAccess: Int, allowField: Int, isInterface: Boolean, fieldExcluded: Int) {
        var flags = classAccess
        var excluded = fieldExcluded
        flags = flags and allowField
        if (!isInterface) excluded = 0
        for (modifier in MODIFIERS.keys) {
            if (flags and modifier == modifier && modifier and excluded == 0) {
                println(MODIFIERS[modifier])
            }
        }
    }

    private fun createClass(classNode: ClassNode): CodeDataStruct {
        val ds = CodeDataStruct()
        ds.NodeName = getDataStructureName(classNode.name)

        val isInterface = CodeConstants.ACC_INTERFACE == classNode.access

        ds.Type = if(isInterface)  DataStructType.INTERFACE else DataStructType.CLASS;

        // todo: add modifiers to Chapi
        appendModifiers(classNode.access, FIELD_ALLOWED, isInterface, FIELD_EXCLUDED)

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