package com.example.blog.ui.register

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.blog.model.User
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterViewModel : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()

    var email = ""
    var password = ""
    var username = ""

    val validationErrorCommand =
        SingleLiveEvent<Void>()

    val isLoading =
        mutableLiveData(View.INVISIBLE)

    val internetCommand =
        SingleLiveEvent<Void>()

    val displayInternetCommand =
        SingleLiveEvent<Void>()

    val registerSuccessful =
        SingleLiveEvent<Void>()

    var isInternetAvailable = false

    fun handleRegisterButtonClick() {
        if (isNetworkConnected()) {
            if (email.isBlank() || password.isBlank() || username.isBlank()) {
                validationErrorCommand.call()
                return
            }

            isLoading.value = View.VISIBLE
            register()
        } else displayNoConnection()
    }

    private fun register() {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User()
                    user.email = email
                    user.name = username

                    FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user)
                        .addOnCompleteListener { databaseTask ->
                            if (databaseTask.isSuccessful) {
                                isLoading.value = View.INVISIBLE
                                registerSuccessful.call()
                            }
                        }
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
