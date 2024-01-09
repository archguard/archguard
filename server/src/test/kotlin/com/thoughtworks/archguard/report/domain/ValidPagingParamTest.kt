package com.thoughtworks.archguard.report.domain;

import com.thoughtworks.archguard.report.controller.coupling.FilterSizingDto
import com.thoughtworks.archguard.report.exception.WrongLimitException
import com.thoughtworks.archguard.report.exception.WrongOffsetException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test

class ValidPagingParamTest {

    @Test
    fun shouldThrowWrongLimitExceptionWhenLimitIsSmallerThanOne() {
        // given
        val limit = 0L
        val offset = 10L

        // when
        val exception = assertThrows(WrongLimitException::class.java) {
            ValidPagingParam.validPagingParam(limit, offset)
        }

        // then
        assertEquals("limit $limit is smaller than 1", exception.message)
    }

    @Test
    fun shouldThrowWrongOffsetExceptionWhenOffsetIsSmallerThanZero() {
        // given
        val limit = 10L
        val offset = -1L

        // when
        val exception = assertThrows(WrongOffsetException::class.java) {
            ValidPagingParam.validPagingParam(limit, offset)
        }

        // then
        assertEquals("offset $offset is smaller than 0", exception.message)
    }

    @Test
    fun shouldReturnValidFilterParamWithValidParams() {
        // given
        val requestFilter = FilterSizingDto(
            currentPageNumber = 1,
            numberPerPage = 10,
            module = "module",
            className = "className",
            packageName = "packageName",
            name = "name"
        )

        // when
        val result = ValidPagingParam.validFilterParam(requestFilter)

        // then
        assertEquals(requestFilter.currentPageNumber, result.currentPageNumber)
        assertEquals(requestFilter.numberPerPage, result.numberPerPage)
        assertEquals(requestFilter.module, result.module)
        assertEquals(requestFilter.className, result.className)
        assertEquals(requestFilter.packageName, result.packageName)
        assertEquals(requestFilter.name, result.name)
    }

    @Test
    fun shouldReturnValidFilterParamWithEmptyParams() {
        // given
        val requestFilter = FilterSizingDto(
            currentPageNumber = 1,
            numberPerPage = 10,
            module = null,
            className = null,
            packageName = null,
            name = null
        )

        // when
        val result = ValidPagingParam.validFilterParam(requestFilter)

        // then
        assertEquals(requestFilter.currentPageNumber, result.currentPageNumber)
        assertEquals(requestFilter.numberPerPage, result.numberPerPage)
        assertEquals("", result.module)
        assertEquals("", result.className)
        assertEquals("", result.packageName)
        assertEquals("", result.name)
    }

    @Test
    fun shouldReturnValidFilterParamWithInvalidParams() {
        // given
        val requestFilter = FilterSizingDto(
            currentPageNumber = 1,
            numberPerPage = 10,
            module = "module'",
            className = "className\"",
            packageName = "packageName",
            name = "name"
        )

        // when
        val result = ValidPagingParam.validFilterParam(requestFilter)

        // then
        assertEquals(requestFilter.currentPageNumber, result.currentPageNumber)
        assertEquals(requestFilter.numberPerPage, result.numberPerPage)
        assertEquals("", result.module)
        assertEquals("", result.className)
        assertEquals(requestFilter.packageName, result.packageName)
        assertEquals(requestFilter.name, result.name)
    }
}

