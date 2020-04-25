package com.example.blog.login

import androidx.lifecycle.ViewModel
import com.example.blog.livedata.mutableLiveData

class LogInViewModel : ViewModel() {
    val isLoginDialogOpen = mutableLiveData(false)

    val username = mutableLiveData<String>(null)

    fun handleLoginButtonClick() {
        isLoginDialogOpen.value = true
    }

    fun handleSuccessfulLogin(username: String) {
        this.username.value = username
        isLoginDialogOpen.value = false
    }

    fun handleCancelledLogin() {
        isLoginDialogOpen.value = false
    }
}