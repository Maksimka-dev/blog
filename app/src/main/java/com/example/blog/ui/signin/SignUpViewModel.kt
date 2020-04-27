package com.example.blog.ui.signin

import androidx.lifecycle.ViewModel
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {
    val isLoginDialogOpen = mutableLiveData(false)

    private val mAuth = FirebaseAuth.getInstance()

    val email =
        mutableLiveData<String>(FirebaseAuth.getInstance().currentUser?.email ?: "Anonymous")

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