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

    fun getToken(): String? {
        return preferences.getString("jwt_token", null)
    }

    fun saveToken(token: String?) {
        preferences.edit().putString("jwt_token", token).apply()
    }
}
