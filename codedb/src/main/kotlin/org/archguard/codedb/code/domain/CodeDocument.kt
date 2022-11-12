package org.archguard.codedb.code.domain;

import chapi.domain.core.CodeDataStruct
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class CodeDocument(
    @Id
    val id: String,
    val systemId: String,
    val language: String,
    val path: String,

    // data_struct
    val ds: CodeDataStruct,
    val ds_package: String,
    val ds_node_name: String,
    val ds_file_path: String,
) {

}
