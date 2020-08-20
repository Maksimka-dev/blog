package com.example.blog.util.view

import com.example.blog.model.Blog

const val MAX_MESSAGE_LENGTH = 500
const val MAX_DOWNLOAD_SIZE_BYTES: Long = 5120 * 5120
const val ID_LENGTH = 25
const val NO_INTERNET = "No internet connection available"
const val WRONG_CREDENTIALS = "Wrong credentials :("
typealias AdapterMessageData = List<String>
typealias AdapterAvatarData = List<String>
typealias AdapterTimeData = List<String>
typealias AdapterBlogData = List<Blog>
