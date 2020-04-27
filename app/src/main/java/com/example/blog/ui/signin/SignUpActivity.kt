package com.example.blog.ui.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.blog.R
import com.example.blog.databinding.ActivitySignupBinding
import com.example.blog.util.inflaters.contentView
import com.example.blog.util.viewmodel.viewModel

class SignUpActivity : AppCompatActivity(),
    SignUpDialogFragment.Listener {
    private val model by viewModel<SignUpViewModel>()
    private val binding by contentView<ActivitySignupBinding>(
        R.layout.activity_signup
    )

    private val loginDialogFragment
        get() = supportFragmentManager.findFragmentByTag("SigninDialog") as? SignUpDialogFragment

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
            ?: SignUpDialogFragment()
                .show(supportFragmentManager, "SigninDialog")
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