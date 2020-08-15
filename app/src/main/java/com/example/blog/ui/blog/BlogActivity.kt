@file:Suppress("DEPRECATION")

package com.example.blog.ui.blog

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.blog.R
import com.example.blog.databinding.ActivityBlogBinding
import com.example.blog.model.Blog
import com.example.blog.ui.chat.ChatActivity
import com.example.blog.ui.newblog.NewBlogDialogFragment
import com.example.blog.util.adapters.BlogListAdapter
import com.example.blog.util.extensions.isInternetAvailable
import com.example.blog.util.extensions.toast
import com.example.blog.util.inflaters.contentView
import com.example.blog.util.view.NO_INTERNET
import com.example.blog.util.viewmodel.viewModel

class BlogActivity : AppCompatActivity(), NewBlogDialogFragment.Listener {
    private val model by viewModel<BlogViewModel>()
    private val binding by contentView<ActivityBlogBinding>(
        R.layout.activity_blog
    )
    private val newBlogDialog
        get() = supportFragmentManager.findFragmentByTag(DIALOG) as? NewBlogDialogFragment

    companion object {
        const val DIALOG = "createDialog"
        const val TITLE = "title"
        const val BLOG_ID = "blogId"
        const val OWNER_ID = "ownerId"
    }

    override fun onResume() {
        super.onResume()
        model.generateItems()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = model

        val adapter = BlogListAdapter(model)
        binding.recyclerView.adapter = adapter

        model.openCommand.observe(this) {
            onOpen(model.blog)
        }

        model.items.observe(this, Observer {
            adapter.setData(it.first, it.second)
        })

        model.isCreateDialogOpen.observe(this, Observer {
            if (it == true) {
                openCreateDialog()
            } else {
                closeCreateDialog()
            }
        })

        model.internetCommand.observe(this) {
            model.isInternetAvailable = isInternetAvailable(this)
        }

        model.displayInternetCommand.observe(this) {
            toast(NO_INTERNET)
        }
    }

    private fun openCreateDialog() {
        newBlogDialog ?: NewBlogDialogFragment()
            .show(supportFragmentManager, DIALOG)
    }

    private fun closeCreateDialog() {
        newBlogDialog?.dismiss()
    }

    private fun onOpen(blog: Blog) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra(TITLE, blog.title)
        intent.putExtra(BLOG_ID, blog.blogId)
        intent.putExtra(OWNER_ID, blog.ownerId)

        startActivity(intent)
    }

    override fun onCreateBlog(title: String) {
        model.handleSuccessfulCreate()
    }

    override fun onCancel() {
        closeCreateDialog()
    }
}
