package com.example.blog.ui.findblog

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.model.Blog
import com.example.blog.model.BlogRepository
import com.example.blog.model.User
import com.example.blog.model.UserRepository
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData

class FindBlogViewModel : ViewModel() {
    val isLoading: MutableLiveData<Int> = mutableLiveData(View.INVISIBLE)
    val subscribeVisibility: MutableLiveData<Int> = mutableLiveData(View.INVISIBLE)
    val search = mutableLiveData("")

    var blog = Blog()
    var isInternetAvailable = true // check!
    var isUserReady = false

    val displayInternetCommand = SingleLiveEvent<Void>()
    val internetCommand = SingleLiveEvent<Void>()
    val searchCommand = SingleLiveEvent<Void>()

    var user: MutableLiveData<User> = mutableLiveData()
    var blogs: MutableLiveData<MutableList<Blog>> = mutableLiveData()
    var blogsAvatars: MutableLiveData<MutableList<String>> = mutableLiveData()
    var subscription: MutableLiveData<Boolean> = mutableLiveData()

    private var blogRepository: BlogRepository = BlogRepository()
    private var userRepository: UserRepository = UserRepository()

    val blogsData = SingleLiveEvent<Pair<List<Blog>, List<String>>>()

    init {
        user = userRepository.user
        blogs = blogRepository.blogsList
        blogsAvatars = blogRepository.blogsAvatarsList
        subscription = blogRepository.subscription
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

    fun handleSearchClick() {
        if (isUserReady) {
            searchCommand.call()
        }
    }

    fun handleBlogClick(blog: Blog) {
        subscribeVisibility.value = View.VISIBLE
        this.blog = blog
    }

    fun handleSubClick() {
        isLoading.value = View.VISIBLE
        blogRepository.subscribe(user.value!!, blog)
    }

    private fun displayNoConnection() {
        displayInternetCommand.call()
    }

    private fun isNetworkConnected(): Boolean {
        internetCommand.call()
        return isInternetAvailable
    }
}
