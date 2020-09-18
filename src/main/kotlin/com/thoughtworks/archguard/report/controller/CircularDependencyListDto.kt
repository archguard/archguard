package com.thoughtworks.archguard.report.controller

data class CircularDependencyListDto<T>(val data: List<List<T>>, val count: Long, val currentPageNumber: Long)