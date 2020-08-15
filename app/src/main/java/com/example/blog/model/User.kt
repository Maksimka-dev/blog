package com.example.blog.model

class User {
    var name: String = ""
    var email: String = ""
    val subbs: ArrayList<String> = arrayListOf()

    override fun toString(): String {
        return this.name
    }
}
