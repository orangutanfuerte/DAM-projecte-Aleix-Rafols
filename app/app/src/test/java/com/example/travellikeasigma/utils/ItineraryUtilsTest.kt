package com.example.travellikeasigma.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [ItineraryUtils].
 * Validates activity title, time, cost, and the combined validateActivity method.
 */
class ItineraryUtilsTest {

    // ── isValidActivityTitle ─────────────────────────────────────────────

    @Test
    fun `isValidActivityTitle returns true for a normal title`() {
        assertTrue(ItineraryUtils.isValidActivityTitle("Visit the Shrine"))
    }

    @Test
    fun `isValidActivityTitle returns false for an empty string`() {
        assertFalse(ItineraryUtils.isValidActivityTitle(""))
    }

    @Test
    fun `isValidActivityTitle returns false for a blank string`() {
        assertFalse(ItineraryUtils.isValidActivityTitle("   "))
    }

    @Test
    fun `isValidActivityTitle returns true for a single character`() {
        assertTrue(ItineraryUtils.isValidActivityTitle("X"))
    }

    // ── isValidActivityTime ──────────────────────────────────────────────

    @Test
    fun `isValidActivityTime returns true for a valid time`() {
        assertTrue(ItineraryUtils.isValidActivityTime("10:30"))
    }

    @Test
    fun `isValidActivityTime returns false for an empty string`() {
        assertFalse(ItineraryUtils.isValidActivityTime(""))
    }

    @Test
    fun `isValidActivityTime returns false for a blank string`() {
        assertFalse(ItineraryUtils.isValidActivityTime("  "))
    }

    // ── isValidActivityCost ──────────────────────────────────────────────

    @Test
    fun `isValidActivityCost returns true for zero`() {
        assertTrue(ItineraryUtils.isValidActivityCost(0.0))
    }

    @Test
    fun `isValidActivityCost returns true for a positive value`() {
        assertTrue(ItineraryUtils.isValidActivityCost(25.50))
    }

    @Test
    fun `isValidActivityCost returns false for a negative value`() {
        assertFalse(ItineraryUtils.isValidActivityCost(-5.0))
    }

    // ── validateActivity (combined validation) ───────────────────────────

    @Test
    fun `validateActivity returns true for valid title and time`() {
        assertTrue(ItineraryUtils.validateActivity("Lunch", "12:00"))
    }

    @Test
    fun `validateActivity returns false when title is empty`() {
        assertFalse(ItineraryUtils.validateActivity("", "12:00"))
    }

    @Test
    fun `validateActivity returns false when time is empty`() {
        assertFalse(ItineraryUtils.validateActivity("Lunch", ""))
    }

    @Test
    fun `validateActivity returns false when both are empty`() {
        assertFalse(ItineraryUtils.validateActivity("", ""))
    }

    @Test
    fun `validateActivity returns false when title is blank`() {
        assertFalse(ItineraryUtils.validateActivity("   ", "09:00"))
    }
}
