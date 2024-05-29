package com.example.smkn1cianjur_06_satriaanandabintang.utils

import android.content.Context
import com.example.smkn1cianjur_06_satriaanandabintang.SharedPrefs

class Constants {
    companion object {
        const val BASE_URL = "http://103.187.147.96"

        fun getHeaders(withToken: Boolean, context: Context? = null): HashMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Application"] = "application/json"
            headers["Accept"] = "application/json"

            if (withToken && context != null) {
                val token = SharedPrefs.getString(context, SharedPrefs.TOKEN)
                headers["Authorization"] = "Bearer $token"
            }

            return headers
        }
    }
}