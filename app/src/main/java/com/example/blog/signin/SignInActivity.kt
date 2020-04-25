package com.example.blog.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.blog.R
import com.example.blog.databinding.ActivitySigninBinding
import com.example.blog.inflaters.contentView
import com.example.blog.viewmodel.viewModel

class SignInActivity : AppCompatActivity()  {
    private val model by viewModel<SignInViewModel>()
    private val binding by contentView<ActivitySigninBinding>(R.layout.activity_signin)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model
    }
}