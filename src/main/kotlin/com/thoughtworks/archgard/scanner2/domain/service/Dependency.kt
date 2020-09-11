package com.thoughtworks.archgard.scanner2.domain.service

data class Dependency<T>(val caller: T, val callee: T)