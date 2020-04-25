package com.example.blog.blog

import android.graphics.Bitmap

data class Blog (var title: String) {
    var ownerId: String = ""
    var time: ArrayList<String> = arrayListOf()
    var description: String = ""
    var blogId: String = ""
    var messages: ArrayList<String> = arrayListOf()
}