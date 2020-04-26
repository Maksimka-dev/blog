package com.example.blog.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.blog.R
import com.example.blog.databinding.ActivitySigninBinding
import com.example.blog.inflaters.contentView
import com.example.blog.viewmodel.viewModel

class SignInActivity : AppCompatActivity(), SignInDialogFragment.Listener  {
    private val model by viewModel<SignInViewModel>()
    private val binding by contentView<ActivitySigninBinding>(R.layout.activity_signin)

    private val loginDialogFragment
        get() = supportFragmentManager.findFragmentByTag("SigninDialog") as? SignInDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model

        model.isLoginDialogOpen.observe(this, Observer {
            if (it == true) {
                openLoginDialog()
            }
            else {
                closeLoginDialog()
            }
        })
    }

    private fun openLoginDialog() {
        loginDialogFragment
            ?: SignInDialogFragment().show(supportFragmentManager, "SigninDialog")
    }

    private fun closeLoginDialog() {
        loginDialogFragment?.dismiss()
    }

    override fun onLogin(email: String) {
        model.handleSuccessfulLogin(email)
    }

    override fun onCancel() {
        model.handleCancelledLogin()
    }
}