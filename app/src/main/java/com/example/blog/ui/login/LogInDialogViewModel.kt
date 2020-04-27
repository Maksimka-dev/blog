package com.example.blog.ui.login

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LogInDialogViewModel : ViewModel() {
    val email = mutableLiveData("")
    val password = mutableLiveData("")

    val remember = mutableLiveData(true)

    val isLoading: MutableLiveData<Int> =
        mutableLiveData(View.INVISIBLE)

    val validationErrorCommand =
        SingleLiveEvent<Void>()

    val loggedInCommand = SingleLiveEvent<Void>()
    val cancelledCommand = SingleLiveEvent<Void>()

    private val mAuth = FirebaseAuth.getInstance()

    fun handleLoginButtonClick() {
        if (email.value.isNullOrBlank() || password.value.isNullOrBlank()) {
            validationErrorCommand.call()
            return
        }

        isLoading.value = View.VISIBLE

        logIn()
    }

    fun handleCancel() {
        cancelledCommand.call()
    }

    private fun logIn(){
        mAuth.signInWithEmailAndPassword(email.value.toString(), password.value.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    loggedInCommand.call()
                    isLoading.value = View.INVISIBLE
                } else {
                    validationErrorCommand.call()
                }
            }
    }
}