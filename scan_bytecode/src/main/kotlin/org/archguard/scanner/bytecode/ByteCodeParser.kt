package org.archguard.scanner.bytecode

import chapi.domain.core.CodeDataStruct
import org.objectweb.asm.ClassReader
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
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
        ds.NodeName = getClassName(classNode.name).toString()

        return ds
    }


    private fun getClassName(internalName: String): String? {
        return Type.getObjectType(internalName).className
    }
}