package com.onkar.androidtest

import org.junit.Assert.assertEquals
import org.junit.Test

/** JVM unit tests for the small pure helpers that drive the UI. */
class StatusLogicTest {

    @Test
    fun status_isReady_beforeAnyClick() {
        assertEquals("Ready", statusForCount(0))
    }

    @Test
    fun status_reportsSuccess_afterClick() {
        assertEquals("Test button works!", statusForCount(1))
        assertEquals("Test button works!", statusForCount(7))
    }

    @Test
    fun counterLabel_formatsCount() {
        assertEquals("Counter: 0", counterLabel(0))
        assertEquals("Counter: 42", counterLabel(42))
    }
}
