@file:Suppress("DEPRECATION")

package com.example.blog.ui.blog

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.model.Blog
import com.example.blog.model.BlogRepository
import com.example.blog.model.User
import com.example.blog.model.UserRepository
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData

class BlogViewModel : ViewModel() {
    val isLoading: MutableLiveData<Int> =
        mutableLiveData(View.VISIBLE)

    var blog = Blog()
    var isInternetAvailable = true // check!

    val displayInternetCommand =
        SingleLiveEvent<Void>()
    val internetCommand =
        SingleLiveEvent<Void>()
    val openCommand =
        SingleLiveEvent<Void>()
    val createBlogCommand =
        SingleLiveEvent<Void>()

    var user: MutableLiveData<User> = mutableLiveData()
    var blogs: MutableLiveData<MutableList<Blog>> = mutableLiveData(mutableListOf())
    var blogsAvatars: MutableLiveData<MutableList<String>> = mutableLiveData()

    private var blogRepository: BlogRepository = BlogRepository()
    private var userRepository: UserRepository = UserRepository()

    val blogsData =
        SingleLiveEvent<Pair<List<Blog>, List<String>>>()

    init {
        user = userRepository.user
        blogs = blogRepository.blogsList
        blogsAvatars = blogRepository.blogsAvatarsList
    }

    fun loadUser() {
        if (isNetworkConnected()) {
            isLoading.value = View.VISIBLE
            userRepository.getUser()
        } else displayNoConnection()
    }

    fun loadBlogs() {
        blogRepository.getBlogs(user.value!!)
    }

    fun loadBlogsPicsUrls() {
        blogRepository.getBlogsAvatars(user.value!!)
    }

    fun prepareData() {
        if (blogs.value != null && blogsAvatars.value != null) {
            blogsData.value = blogs.value!! to blogsAvatars.value!!

            isLoading.value = View.INVISIBLE
        }
    }

    fun handleOpenBlogCLick(currentBlog: Blog) {
        blog = currentBlog
        openCommand.call()
    }

    fun handleCreateBlogButtonClick() {
        createBlogCommand.call()
    }

    private fun displayNoConnection() {
        displayInternetCommand.call()
    }

    private fun isNetworkConnected(): Boolean {
        internetCommand.call()
        return isInternetAvailable
    }
}
