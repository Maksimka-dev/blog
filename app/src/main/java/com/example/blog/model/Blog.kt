package com.example.blog.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Blog(
    var title: String = "",
    var ownerId: String = "",
    var time: ArrayList<String> = arrayListOf(),
    var description: String = "",
    var blogId: String = "",
    var messages: ArrayList<String> = arrayListOf()
) : Parcelable
