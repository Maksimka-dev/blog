@file:Suppress("DEPRECATION")

package com.example.blog.ui.blog

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.model.blog.Blog
import com.example.blog.model.blog.BlogRepository
import com.example.blog.model.user.User
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import javax.inject.Inject

class BlogViewModel @Inject constructor(val blogRepository: BlogRepository) : ViewModel() {
    val isLoading: MutableLiveData<Int> =
        mutableLiveData(View.VISIBLE)

    var blog = Blog()

    val openCommand =
        SingleLiveEvent<Void>()
    val createBlogCommand =
        SingleLiveEvent<Void>()

    var user: MutableLiveData<User> = mutableLiveData()
    var blogs: MutableLiveData<MutableList<Blog>> = mutableLiveData()
    var blogsAvatars: MutableLiveData<MutableList<String>> = mutableLiveData()

    val blogsData =
        SingleLiveEvent<Pair<List<Blog>, List<String>>>()

    init {
        blogs = blogRepository.blogsList
        blogsAvatars = blogRepository.blogsAvatarsList
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
}
