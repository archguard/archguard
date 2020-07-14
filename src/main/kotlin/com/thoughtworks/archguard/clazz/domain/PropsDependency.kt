package com.thoughtworks.archguard.clazz.domain

import com.thoughtworks.archguard.module.domain.model.Dependency

class PropsDependency<T>(caller: T, callee: T, val count: Int, val props: Map<String, Boolean>) : Dependency<T>(caller, callee) {

}
