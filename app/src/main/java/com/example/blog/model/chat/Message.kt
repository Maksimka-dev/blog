package com.example.blog.model.chat

import android.graphics.Bitmap

data class Message(
    val text: String,
    val time: String,
    val picUrl: String,
    val pic: Bitmap? = null
)
