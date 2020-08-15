package com.example.blog.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.blog.R
import com.example.blog.databinding.ActivityLoginBinding
import com.example.blog.util.inflaters.contentView
import com.example.blog.util.viewmodel.viewModel

class LogInActivity : AppCompatActivity(),
    LogInDialogFragment.Listener {
    private val model by viewModel<LogInViewModel>()
    private val binding by contentView<ActivityLoginBinding>(
        R.layout.activity_login
    )
    private val loginDialogFragment
        get() = supportFragmentManager.findFragmentByTag(DIALOG) as? LogInDialogFragment

    companion object {
        const val DIALOG = "LoginDialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model

        model.isLoginDialogOpen.observe(this, Observer {
            if (it == true) {
                openLoginDialog()
            } else {
                closeLoginDialog()
            }
        })
    }

    private fun openLoginDialog() {
        loginDialogFragment
            ?: LogInDialogFragment()
                .show(supportFragmentManager, DIALOG)
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
