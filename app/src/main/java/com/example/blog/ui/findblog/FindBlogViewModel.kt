package com.example.blog.ui.findblog

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.model.blog.Blog
import com.example.blog.model.blog.BlogRepository
import com.example.blog.model.user.User
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import javax.inject.Inject

class FindBlogViewModel @Inject constructor(val blogRepository: BlogRepository) : ViewModel() {
    val isLoading: MutableLiveData<Int> = mutableLiveData(View.INVISIBLE)
    val subscribeVisibility: MutableLiveData<Int> = mutableLiveData(View.INVISIBLE)
    val search = mutableLiveData("")

    var blog = Blog()
    var isUserReady = false

    val searchCommand = SingleLiveEvent<Void>()

    var user: MutableLiveData<User> = mutableLiveData()
    var blogs: MutableLiveData<MutableList<Blog>> = mutableLiveData()
    var blogsAvatars: MutableLiveData<MutableList<String>> = mutableLiveData()
    var subscription: MutableLiveData<Boolean> = mutableLiveData()

    val blogsData = SingleLiveEvent<Pair<List<Blog>, List<String>>>()

    init {
        blogs = blogRepository.blogsList
        blogsAvatars = blogRepository.blogsAvatarsList
        subscription = blogRepository.subscription
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
}
