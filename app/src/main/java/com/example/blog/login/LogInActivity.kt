package com.example.blog.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blog.R
import com.example.blog.databinding.ActivityLoginBinding
import com.example.blog.viewmodel.viewModel
import com.example.blog.inflaters.contentView

class LogInActivity : AppCompatActivity()  {
    private val model by viewModel<LogInViewModel>()
    private val binding by contentView<ActivityLoginBinding>(R.layout.activity_login)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model
    }
}