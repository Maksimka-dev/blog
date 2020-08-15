package com.example.blog.ui.findblog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.blog.R
import com.example.blog.databinding.ActivityFindblogBinding
import com.example.blog.util.adapters.FindBlogListAdapter
import com.example.blog.util.inflaters.contentView
import com.example.blog.util.viewmodel.viewModel

class FindBlogActivity : AppCompatActivity() {
    private val model by viewModel<FindBlogViewModel>()
    private val binding by contentView<ActivityFindblogBinding>(
        R.layout.activity_findblog
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = model

        val adapter = FindBlogListAdapter(model)
        binding.recyclerView.adapter = adapter

        model.searchCommand.observe(this) {
            model.generateItems()
        }

        model.items.observe(this, Observer {
            adapter.setData(it.first, it.second)
        })

        model.subCommand.observe(this) {
            model.subscribe()
        }
    }
}
