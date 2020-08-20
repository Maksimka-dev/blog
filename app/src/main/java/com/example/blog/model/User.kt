package com.example.blog.model

data class User(
    var name: String = "",
    var email: String = "",
    val subbs: ArrayList<String> = arrayListOf()
)
