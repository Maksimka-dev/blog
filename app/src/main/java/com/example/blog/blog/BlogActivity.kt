package com.example.blog.blog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blog.R
import com.example.blog.databinding.ActivityBlogBinding
import com.example.blog.inflaters.contentView
import com.example.blog.viewmodel.viewModel

class BlogActivity : AppCompatActivity() {
    private val model by viewModel<BlogViewModel>()
    private val binding by contentView<ActivityBlogBinding>(R.layout.activity_blog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model
    }
}