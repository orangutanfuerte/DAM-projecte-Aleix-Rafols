package com.example.travellikeasigma.utils

import android.util.Log

/**
 * Utility methods for ItineraryActivity validation.
 * Centralizes all activity-related validation logic so it can be
 * reused across ViewModels, screens, and unit tests.
 */
object ItineraryUtils {

    private const val TAG = "ItineraryUtils"

    /** A valid activity title must not be blank. */
    fun isValidActivityTitle(title: String): Boolean {
        val valid = title.isNotBlank()
        if (!valid) Log.w(TAG, "Invalid activity title: title is blank")
        return valid
    }

    /** A valid activity time must not be blank. */
    fun isValidActivityTime(time: String): Boolean {
        val valid = time.isNotBlank()
        if (!valid) Log.w(TAG, "Invalid activity time: time is blank")
        return valid
    }

    /** A valid activity cost must be non-negative. */
    fun isValidActivityCost(cost: Double): Boolean {
        val valid = cost >= 0.0
        if (!valid) Log.w(TAG, "Invalid activity cost: $cost (must be >= 0)")
        return valid
    }

    /**
     * Full validation for an activity.
     * Checks that both title and time are non-blank.
     */
    fun validateActivity(title: String, time: String): Boolean {
        return isValidActivityTitle(title) && isValidActivityTime(time)
    }
}
