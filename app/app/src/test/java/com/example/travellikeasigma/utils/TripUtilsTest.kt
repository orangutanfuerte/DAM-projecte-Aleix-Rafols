package com.example.travellikeasigma.utils

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/**
 * Unit tests for [TripUtils].
 * Validates trip name, date ranges, person count, and the combined validateNewTrip method.
 */
class TripUtilsTest {

    // ── isValidTripName ──────────────────────────────────────────────────

    @Test
    fun `isValidTripName returns true for a normal name`() {
        assertTrue(TripUtils.isValidTripName("Japan Trip"))
    }

    @Test
    fun `isValidTripName returns false for an empty string`() {
        assertFalse(TripUtils.isValidTripName(""))
    }

    @Test
    fun `isValidTripName returns false for a blank string with spaces`() {
        assertFalse(TripUtils.isValidTripName("   "))
    }

    @Test
    fun `isValidTripName returns true for a single character`() {
        assertTrue(TripUtils.isValidTripName("A"))
    }

    // ── areDatesValid ────────────────────────────────────────────────────

    @Test
    fun `areDatesValid returns true when endDate equals startDate`() {
        val date = LocalDate.of(2026, 6, 15)
        assertTrue(TripUtils.areDatesValid(date, date))
    }

    @Test
    fun `areDatesValid returns true when endDate is after startDate`() {
        val start = LocalDate.of(2026, 6, 1)
        val end = LocalDate.of(2026, 6, 10)
        assertTrue(TripUtils.areDatesValid(start, end))
    }

    @Test
    fun `areDatesValid returns false when endDate is before startDate`() {
        val start = LocalDate.of(2026, 6, 10)
        val end = LocalDate.of(2026, 6, 1)
        assertFalse(TripUtils.areDatesValid(start, end))
    }

    @Test
    fun `areDatesValid returns false when startDate is null`() {
        assertFalse(TripUtils.areDatesValid(null, LocalDate.of(2026, 6, 1)))
    }

    @Test
    fun `areDatesValid returns false when endDate is null`() {
        assertFalse(TripUtils.areDatesValid(LocalDate.of(2026, 6, 1), null))
    }

    @Test
    fun `areDatesValid returns false when both dates are null`() {
        assertFalse(TripUtils.areDatesValid(null, null))
    }

    // ── isStartDateNotInPast ─────────────────────────────────────────────

    @Test
    fun `isStartDateNotInPast returns true for a future date`() {
        val futureDate = LocalDate.now().plusDays(30)
        assertTrue(TripUtils.isStartDateNotInPast(futureDate))
    }

    @Test
    fun `isStartDateNotInPast returns true for today`() {
        assertTrue(TripUtils.isStartDateNotInPast(LocalDate.now()))
    }

    @Test
    fun `isStartDateNotInPast returns false for yesterday`() {
        val yesterday = LocalDate.now().minusDays(1)
        assertFalse(TripUtils.isStartDateNotInPast(yesterday))
    }

    // ── isValidPersonCount ───────────────────────────────────────────────

    @Test
    fun `isValidPersonCount returns true for 1 person`() {
        assertTrue(TripUtils.isValidPersonCount(1))
    }

    @Test
    fun `isValidPersonCount returns true for multiple persons`() {
        assertTrue(TripUtils.isValidPersonCount(5))
    }

    @Test
    fun `isValidPersonCount returns false for 0 persons`() {
        assertFalse(TripUtils.isValidPersonCount(0))
    }

    @Test
    fun `isValidPersonCount returns false for negative persons`() {
        assertFalse(TripUtils.isValidPersonCount(-1))
    }

    // ── validateNewTrip (combined validation) ────────────────────────────

    @Test
    fun `validateNewTrip returns true for valid inputs`() {
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 10)
        assertTrue(TripUtils.validateNewTrip("Summer Trip", start, end))
    }

    @Test
    fun `validateNewTrip returns false when name is blank`() {
        val start = LocalDate.of(2026, 7, 1)
        val end = LocalDate.of(2026, 7, 10)
        assertFalse(TripUtils.validateNewTrip("", start, end))
    }

    @Test
    fun `validateNewTrip returns false when dates are null`() {
        assertFalse(TripUtils.validateNewTrip("Trip", null, null))
    }

    @Test
    fun `validateNewTrip returns false when endDate is before startDate`() {
        val start = LocalDate.of(2026, 7, 10)
        val end = LocalDate.of(2026, 7, 1)
        assertFalse(TripUtils.validateNewTrip("Trip", start, end))
    }

    @Test
    fun `validateNewTrip returns false when name is blank and dates are invalid`() {
        assertFalse(TripUtils.validateNewTrip("  ", null, LocalDate.of(2026, 1, 1)))
    }
}
