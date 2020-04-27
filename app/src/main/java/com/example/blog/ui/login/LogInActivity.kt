package com.example.blog.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.blog.R
import com.example.blog.databinding.ActivityLoginBinding
import com.example.blog.util.viewmodel.viewModel
import com.example.blog.util.inflaters.contentView

class LogInActivity : AppCompatActivity(),
    LogInDialogFragment.Listener {
    private val model by viewModel<LogInViewModel>()
    private val binding by contentView<ActivityLoginBinding>(
        R.layout.activity_login
    )

    private val loginDialogFragment
        get() = supportFragmentManager.findFragmentByTag("LoginDialog") as? LogInDialogFragment

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
            ?: LogInDialogFragment()
                .show(supportFragmentManager, "LoginDialog")
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