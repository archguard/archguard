package com.thoughtworks.archguard.report.domain.badsmell

enum class BadSmellType(var value: String) {
    DATACLUMPS("数据泥团"),
    DEEPINHERITANCE("过深继承"),
    CLASSHUB("枢纽类"),
    METHODHUB("枢纽方法"),
    PACKAGEHUB("枢纽包"),
    MODULEHUB("枢纽模块"),
    CYCLEDEPENDENCY("循环依赖"),

    SIZINGMODULES("子模块过大"),
    SIZINGPACKAGE("包过大"),
    SIZINGMETHOD("方法过大"),
    SIZINGCLASS("类过大")
}

enum class BadSmellLevel {
    A, B, C, D
}