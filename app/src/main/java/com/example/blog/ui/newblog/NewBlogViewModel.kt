package com.example.blog.ui.newblog

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.model.Blog
import com.example.blog.model.BlogRepository
import com.example.blog.model.User
import com.example.blog.model.UserRepository
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.example.blog.util.view.ID_LENGTH
import com.google.firebase.auth.FirebaseAuth

class NewBlogViewModel : ViewModel() {
    var title: MutableLiveData<String> = mutableLiveData("")
    var description: MutableLiveData<String> = mutableLiveData("")
    val isLoading: MutableLiveData<Int> = mutableLiveData(View.INVISIBLE)

    val validationErrorCommand = SingleLiveEvent<Void>()
    val avatarCommand = SingleLiveEvent<Void>()
    val displayInternetCommand = SingleLiveEvent<Void>()
    val internetCommand = SingleLiveEvent<Void>()

    var isInternetAvailable = true
    var bitmapImage: Bitmap? = null
    var isUserReady = false

    var user: MutableLiveData<User> = mutableLiveData()
    var subscription: MutableLiveData<Boolean> = mutableLiveData()
    var createdBlog: MutableLiveData<Blog> = mutableLiveData()

    private val userRepository = UserRepository()
    private val blogRepository = BlogRepository()

    init {
        user = userRepository.user
        subscription = blogRepository.subscription
        createdBlog = blogRepository.createdBlog
    }

    fun getUser() {
        userRepository.getUser()
    }

    fun handleCreateButtonClick() {
        if (isNetworkConnected()) {
            if (bitmapImage != null && user.value != null) {
                if (title.value.isNullOrBlank()) {
                    validationErrorCommand.call()
                    return
                }
                isLoading.value = View.VISIBLE

                createBlog()
            } else displayNoAvatar()
        } else displayNoConnection()
    }

    private fun createBlog() {
        if (bitmapImage != null) {
            val blog = Blog()
            blog.title = title.value.toString()
            blog.description = description.value.toString()
            blog.ownerId = FirebaseAuth.getInstance().currentUser!!.uid

            val source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
            blog.blogId = (1..ID_LENGTH)
                .map { source.random() }
                .joinToString("")

            blogRepository.createBlog(blog, bitmapImage!!)
        }
    }

    fun subscribe(blog: Blog) {
        blogRepository.subscribe(user.value!!, blog)
    }

    private fun displayNoAvatar() {
        avatarCommand.call()
    }

    private fun displayNoConnection() {
        displayInternetCommand.call()
    }

    private fun isNetworkConnected(): Boolean {
        internetCommand.call()
        return isInternetAvailable
    }
}
