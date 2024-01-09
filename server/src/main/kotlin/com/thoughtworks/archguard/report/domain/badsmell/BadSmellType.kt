package com.thoughtworks.archguard.report.domain.badsmell

import com.thoughtworks.archguard.report.domain.overview.calculator.BadSmellLevelCalculator

enum class BadSmellType(val value: String, var badSmellCalculator: BadSmellLevelCalculator?) {
    DATACLUMPS("数据泥团", null),
    DEEPINHERITANCE("过深继承", null),
    CLASSHUB("枢纽类", null),
    METHODHUB("枢纽方法", null),
    PACKAGEHUB("枢纽包", null),
    MODULEHUB("枢纽模块", null),
    CYCLEDEPENDENCY("循环依赖", null),

    SIZINGMODULES("子模块过大", null),
    SIZINGPACKAGE("包过大", null),
    SIZINGMETHOD("方法过大", null),
    SIZINGCLASS("类过大", null),

    DATA_CLASS("数据类", null),
    SHOTGUN_SURGERY("散弹式修改", null),

    REDUNDANT_ELEMENT("冗余元素", null),
    OVER_GENERALIZATION("过度泛化", null),

    SLEEP_TEST("含Sleep的测试", null),
    IGNORE_TEST("被忽略的测试", null),
    UN_ASSERT_TEST("缺乏校验的测试", null);
}

