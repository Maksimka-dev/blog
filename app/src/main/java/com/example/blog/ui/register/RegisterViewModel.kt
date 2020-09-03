package com.example.blog.ui.register

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.blog.model.storage.SharedPreferencesStorage
import com.example.blog.model.user.User
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.example.blog.util.view.PASSWORD_SUFFIX
import com.example.blog.util.view.REGISTERED_USER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    val mAuth: FirebaseAuth,
    val database: FirebaseDatabase,
    val sharedPreferencesStorage: SharedPreferencesStorage
) : ViewModel() {

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
                    val user = User(email = email, name = username)

                    database
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user)
                        .addOnCompleteListener { databaseTask ->
                            if (databaseTask.isSuccessful) {

                                sharedPreferencesStorage.setString(
                                    REGISTERED_USER,
                                    email
                                )
                                sharedPreferencesStorage.setString(
                                    "$email$PASSWORD_SUFFIX",
                                    password
                                )

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
