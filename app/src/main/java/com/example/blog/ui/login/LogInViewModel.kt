package com.example.blog.ui.login

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.example.blog.model.storage.SharedPreferencesStorage
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.example.blog.util.view.PASSWORD_SUFFIX
import com.example.blog.util.view.REGISTERED_USER
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LogInViewModel @Inject constructor(
    val mAuth: FirebaseAuth,
    val sharedPreferencesStorage: SharedPreferencesStorage
) : ViewModel() {

    var password = ""
    val emailTV = ObservableField(sharedPreferencesStorage.getString(REGISTERED_USER))

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
            if (emailTV.get()!!.isBlank() || password.isBlank()) {
                validationErrorCommand.call()
                return
            }

            isLoading.value = View.VISIBLE
            logIn()
        } else displayNoConnection()
    }

    private fun logIn() {
        mAuth.signInWithEmailAndPassword(emailTV.get()!!, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    sharedPreferencesStorage.setString(
                        REGISTERED_USER,
                        emailTV.get()!!
                    )
                    sharedPreferencesStorage.setString(
                        "${emailTV.get()!!}$PASSWORD_SUFFIX",
                        password
                    )

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
