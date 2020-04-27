@file:Suppress("DEPRECATION")

package com.example.blog.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blog.R
import com.example.blog.databinding.ActivityMainBinding
import com.example.blog.ui.blog.BlogActivity
import com.example.blog.ui.findblog.FindBlogActivity
import com.example.blog.ui.login.LogInActivity
import com.example.blog.ui.signin.SignUpActivity
import com.example.blog.util.inflaters.contentView


class MainActivity : AppCompatActivity() {

    private val binding by contentView<ActivityMainBinding>(
        R.layout.activity_main
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setButtons()
    }

    private fun setButtons() {
        binding.homeButton.setOnClickListener {
            startActivity(Intent(this, BlogActivity::class.java))
        }

        binding.findButton.setOnClickListener {
            startActivity(Intent(this, FindBlogActivity::class.java))
        }

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
