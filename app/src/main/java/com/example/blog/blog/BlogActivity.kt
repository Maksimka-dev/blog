package com.example.blog.blog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blog.R
import com.example.blog.databinding.ActivityBlogBinding
import com.example.blog.inflaters.contentView
import com.example.blog.newblog.NewBlogDialogFragment
import com.example.blog.ui.chat.ChatFragment
import com.example.blog.viewmodel.viewModel

class BlogActivity : AppCompatActivity(), NewBlogDialogFragment.Listener{
    private val model by viewModel<BlogViewModel>()
    private val binding by contentView<ActivityBlogBinding>(R.layout.activity_blog)

    private val newBlogDialog
        get() = supportFragmentManager.findFragmentByTag("createDialog") as? NewBlogDialogFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model

        val adapter = BlogListAdapter(model)

        model.generateItems()

        model.openCommand.observe(this){
            onOpen(model.blog)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        model.items.observe(this, Observer {
            adapter.setData(it.first, it.second)
        })

        model.isCreateDialogOpen.observe(this, Observer {
            if (it == true) {
                openCreateDialog()
            }
            else {
                closeCreateDialog()
            }
        })
    }

    private fun openCreateDialog(){
        newBlogDialog ?: NewBlogDialogFragment().show(supportFragmentManager, "createDialog")
    }

    private fun closeCreateDialog(){
        newBlogDialog?.dismiss()
    }

    private fun onOpen(blog: Blog) {
        val bundle = Bundle()
        bundle.putString("title", blog.title)
        bundle.putString("blogId", blog.blogId)
        bundle.putString("ownerId", blog.ownerId)

        val fragment = ChatFragment.newInstance()
        fragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onCreateBlog(title: String) {
        model.handleSuccessfulCreate()
    }

    override fun onCancel() {
        closeCreateDialog()
    }
}
