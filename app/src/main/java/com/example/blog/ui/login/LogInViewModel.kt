package com.example.blog.ui.login

import androidx.lifecycle.ViewModel
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LogInViewModel : ViewModel() {
    val isLoginDialogOpen = mutableLiveData(false)

    private val mAuth = FirebaseAuth.getInstance()

    val email =
        mutableLiveData(FirebaseAuth.getInstance().currentUser?.email ?: "Anonymous")

    fun handleLoginButtonClick() {
        isLoginDialogOpen.value = true
    }

    fun handleSuccessfulLogin(email: String) {
        this.email.value = email
        isLoginDialogOpen.value = false
    }

    fun handleCancelledLogin() {
        isLoginDialogOpen.value = false
    }

    fun handleLogoutClick(){
        mAuth.signOut()
        email.value = "Anonymous"
    }
}