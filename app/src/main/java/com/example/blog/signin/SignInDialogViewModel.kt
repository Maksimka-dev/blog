package com.example.blog.signin

import androidx.lifecycle.ViewModel
import com.example.blog.livedata.SingleLiveEvent
import com.example.blog.livedata.mutableLiveData
import java.util.*
import kotlin.concurrent.schedule

class SignInDialogViewModel : ViewModel() {
    val username = mutableLiveData("")
    val password = mutableLiveData("")

    val remember = mutableLiveData(true)

    val isLoading = mutableLiveData(false)

    val validationErrorCommand = SingleLiveEvent<Void>()

    val loggedInCommand = SingleLiveEvent<Void>()
    val cancelledCommand = SingleLiveEvent<Void>()

    fun handleLoginButtonClick() {
        if (username.value.isNullOrBlank() || password.value.isNullOrBlank()) {
            validationErrorCommand.call()
            return
        }

        isLoading.value = true

        Timer().schedule(3000) {
            //uiThread {
            isLoading.value = false
            loggedInCommand.call()
            //}
        }
    }

    fun handleCancel() {
        cancelledCommand.call()
    }
}