package com.example.travellikeasigma.utils

import android.util.Log
import java.time.LocalDate

/**
 * Utility methods for Trip validation.
 * Centralizes all trip-related validation logic so it can be
 * reused across ViewModels, screens, and unit tests.
 */
object TripUtils {

    private const val TAG = "TripUtils"

    /** A valid trip name must not be blank. */
    fun isValidTripName(name: String): Boolean {
        val valid = name.isNotBlank()
        if (!valid) Log.w(TAG, "Invalid trip name: name is blank")
        return valid
    }

    /** Both dates must be non-null and endDate must not be before startDate. */
    fun areDatesValid(startDate: LocalDate?, endDate: LocalDate?): Boolean {
        if (startDate == null || endDate == null) {
            Log.w(TAG, "Invalid dates: startDate=$startDate, endDate=$endDate")
            return false
        }
        if (endDate.isBefore(startDate)) {
            Log.w(TAG, "Invalid dates: endDate ($endDate) is before startDate ($startDate)")
            return false
        }
        return true
    }

    /** The start date must not be in the past (before today). */
    fun isStartDateNotInPast(startDate: LocalDate): Boolean {
        val valid = !startDate.isBefore(LocalDate.now())
        if (!valid) Log.w(TAG, "Start date $startDate is in the past")
        return valid
    }

    /** The number of persons must be at least 1. */
    fun isValidPersonCount(persons: Int): Boolean {
        val valid = persons >= 1
        if (!valid) Log.w(TAG, "Invalid person count: $persons (must be >= 1)")
        return valid
    }

    /**
     * Full validation for creating a new trip.
     * Checks name, dates, and that endDate is not before startDate.
     */
    fun validateNewTrip(name: String, startDate: LocalDate?, endDate: LocalDate?): Boolean {
        return isValidTripName(name) && areDatesValid(startDate, endDate)
    }
}
