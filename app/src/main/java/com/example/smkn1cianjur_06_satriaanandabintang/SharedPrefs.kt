package com.example.smkn1cianjur_06_satriaanandabintang

import android.content.Context
import android.content.SharedPreferences

class SharedPrefs {
    companion object {
        private const val PREF_NAME = "lksmart_pref"
        const val TOKEN = "token"

        private fun getSharedPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        fun getString(context: Context, key: String, value: String? = null): String {
            return getSharedPreferences(context).getString(key, value) ?: ""
        }

        fun putString(context: Context, key: String, value: String) {
            val editor = getSharedPreferences(context).edit()
            editor.putString(key, value)
            editor.apply()
        }
    }
}