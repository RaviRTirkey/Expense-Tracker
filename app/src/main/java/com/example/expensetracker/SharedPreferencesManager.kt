package com.example.expensetracker

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SharedPreferencesManager {

    private val PREF_NAME = "UserPrefs"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"
    private val KEY_USER_ID = "userId"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    // Save login state
    fun saveLoginState(context: Context, isLoggedIn: Boolean, userId: String) {
        getPreferences(context).edit() {
            putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
            putString(KEY_USER_ID, userId)
        }
    }

    // Check login state
    fun isLoggedIn(context: Context): Boolean {
        return getPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    // Get the user ID (if logged in)
    fun getUserId(context: Context): String? {
        return getPreferences(context).getString(KEY_USER_ID, null)
    }

    // Clear login state (e.g., on logout)
    fun clearLoginState(context: Context) {
        getPreferences(context).edit() {
            putBoolean(KEY_IS_LOGGED_IN, false)
            remove(KEY_USER_ID)
        }
    }
}