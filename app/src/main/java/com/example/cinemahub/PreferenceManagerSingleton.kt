package com.example.cinemahub

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Singleton


@Singleton
object PreferenceManagerSingleton {
    private lateinit var preferences: SharedPreferences

    fun init(context: Context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun saveToken(token: String?) {
        preferences.edit().putString("jwt_token", token).apply()
    }

    fun getToken(): String {
        return preferences.getString("jwt_token", null) ?: ""
    }

    fun saveUsername(username: String?) {
        preferences.edit().putString("username", username).apply()
    }

    fun getUsername(): String {
        return preferences.getString("username", null) ?: ""
    }

    fun saveUserId(userId: Int) {
        preferences.edit().putInt("user_id", userId).apply()
    }

    fun getUserId(): Int {
        return preferences.getInt("user_id", -1)
    }

    fun saveAudience(audience: String) {
        preferences.edit().putString("audience", audience).apply()
    }

    fun getAudience(): String {
        return preferences.getString("audience", null) ?: ""
    }

    fun logOut() {
        preferences.edit().remove("jwt_token").apply()
        preferences.edit().remove("username").apply()
        preferences.edit().remove("user_id").apply()
        preferences.edit().remove("audience").apply()
    }
}
