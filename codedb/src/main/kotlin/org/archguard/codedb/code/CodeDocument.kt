package org.archguard.codedb.code;

import chapi.domain.core.CodeDataStruct
import org.springframework.data.mongodb.core.mapping.Document

@Document
class CodeDocument(
    val id: String,
    val systemId: String,
    val language: String,
    val path: String,
    val ds: CodeDataStruct
) {

}
