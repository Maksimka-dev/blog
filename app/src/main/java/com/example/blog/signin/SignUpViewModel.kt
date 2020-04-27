package com.example.blog.signin

import androidx.lifecycle.ViewModel
import com.example.blog.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth

class SignUpViewModel : ViewModel() {
    val isLoginDialogOpen = mutableLiveData(false)

    val email = mutableLiveData<String>(FirebaseAuth.getInstance().currentUser?.email)

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
}