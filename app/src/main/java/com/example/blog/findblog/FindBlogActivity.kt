package com.example.blog.findblog

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blog.R
import com.example.blog.databinding.ActivityFindblogBinding
import com.example.blog.inflaters.contentView
import com.example.blog.viewmodel.viewModel

class FindBlogActivity : AppCompatActivity()  {
    private val model by viewModel<FindBlogViewModel>()
    private val binding by contentView<ActivityFindblogBinding>(R.layout.activity_findblog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model
    }
}