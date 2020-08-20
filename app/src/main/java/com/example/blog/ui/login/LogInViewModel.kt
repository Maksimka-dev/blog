package com.example.blog.ui.login

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth

class LogInViewModel : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()

    var email = ""
    var password = ""

    val validationErrorCommand =
        SingleLiveEvent<Void>()

    val isLoading =
        mutableLiveData(View.INVISIBLE)

    val internetCommand =
        SingleLiveEvent<Void>()

    val displayInternetCommand =
        SingleLiveEvent<Void>()

    val loginSuccessful =
        SingleLiveEvent<Void>()

    var isInternetAvailable = false

    init {
        if (mAuth.currentUser != null) {
            loginSuccessful.call()
        }
    }

    fun handleLoginButtonClick() {
        if (isNetworkConnected()) {
            if (email.isBlank() || password.isBlank()) {
                validationErrorCommand.call()
                return
            }

            isLoading.value = View.VISIBLE
            logIn()
        } else displayNoConnection()
    }

    private fun logIn() {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    loginSuccessful.call()
                    isLoading.value = View.INVISIBLE
                } else {
                    validationErrorCommand.call()
                }
            }
    }

    private fun displayNoConnection() {
        displayInternetCommand.call()
    }

    private fun isNetworkConnected(): Boolean {
        internetCommand.call()
        return isInternetAvailable
    }
}
