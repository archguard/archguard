package com.thoughtworks.archguard.report.domain.model

class ClassSizing(val systemId: Long,
                  val moduleName: String? = null,
                  val packageName: String,
                  val typeName: String,
                  val lines: Int)