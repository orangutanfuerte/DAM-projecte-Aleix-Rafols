package com.example.travellikeasigma.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    @ApplicationContext context: Context
) {
    private val prefs = context.getSharedPreferences("travel_sigma_prefs", Context.MODE_PRIVATE)

    var isLoggedIn: Boolean
        get() = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()

    var loggedInEmail: String?
        get() = prefs.getString(KEY_LOGGED_IN_EMAIL, null)
        set(value) = prefs.edit().putString(KEY_LOGGED_IN_EMAIL, value).apply()

    var loggedInUid: String?
        get() = prefs.getString(KEY_LOGGED_IN_UID, null)
        set(value) = prefs.edit().putString(KEY_LOGGED_IN_UID, value).apply()

    var themeMode: String
        get() = prefs.getString(KEY_THEME_MODE, "system") ?: "system"
        set(value) = prefs.edit().putString(KEY_THEME_MODE, value).apply()

    var notificationsEnabled: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATIONS_ENABLED, true)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATIONS_ENABLED, value).apply()

    var language: String
        get() {
            val saved = prefs.getString(KEY_LANGUAGE, null)
            if (saved != null) return saved
            val deviceLang = java.util.Locale.getDefault().language
            return if (deviceLang in listOf("en", "es", "ca")) deviceLang else "en"
        }
        set(value) = prefs.edit().putString(KEY_LANGUAGE, value).apply()

    fun clearSession() {
        prefs.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_LOGGED_IN_EMAIL)
            .remove(KEY_LOGGED_IN_UID)
            .apply()
    }

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_LOGGED_IN_EMAIL = "logged_in_email"
        private const val KEY_LOGGED_IN_UID = "logged_in_uid"
        private const val KEY_THEME_MODE = "theme_mode"
        private const val KEY_NOTIFICATIONS_ENABLED = "notifications_enabled"
        private const val KEY_LANGUAGE = "language"
    }
}
