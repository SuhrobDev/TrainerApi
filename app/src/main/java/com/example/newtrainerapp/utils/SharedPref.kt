package com.example.newtrainerapp.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences

class SharedPref(context: Context) {

    var preferences: SharedPreferences =
        context.getSharedPreferences("APP_PREFS_NAME", MODE_PRIVATE)

    private lateinit var editor: SharedPreferences.Editor

    fun setLang(lang: String) {
        editor = preferences.edit()
        editor.putString("LANG", lang)
        editor.apply()
    }

    fun getLang() = preferences.getString("LANG", "ru")


    fun setToken(token: String) {
        editor = preferences.edit()
        editor.putString("TOKEN", token)
        editor.apply()
    }

    fun getToken() = preferences.getString("TOKEN", "")

    fun isLangSelected(isSelected: Boolean) {
        editor = preferences.edit()
        editor.putBoolean("IS_SELECTED", isSelected)
        editor.apply()
    }

    fun setUserName(username: String) {
        editor = preferences.edit()
        editor.putString("UserName", username)
        editor.apply()
    }

    fun getUserName() = preferences.getString("UserName", "")

    fun setPassword(password: String) {
        editor = preferences.edit()
        editor.putString("password", password)
        editor.apply()
    }

    fun getPassword() = preferences.getString("password", "")

    fun getLangSelected() = preferences.getBoolean("IS_SELECTED", false)

}
