package com.example.blog.model.storage

import android.content.Context
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(context: Context) {

    private val sharedPreferences = context.getSharedPreferences("Blog", Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }
}
