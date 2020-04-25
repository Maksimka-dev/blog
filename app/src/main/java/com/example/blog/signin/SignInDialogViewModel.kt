package com.example.blog.signin

import androidx.lifecycle.ViewModel
import com.example.blog.livedata.SingleLiveEvent
import com.example.blog.livedata.mutableLiveData
import com.example.blog.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignInDialogViewModel : ViewModel() {
    val username = mutableLiveData("")
    val password = mutableLiveData("")
    val email = mutableLiveData("")

    val remember = mutableLiveData(true)

    val isLoading = mutableLiveData(false)

    val validationErrorCommand = SingleLiveEvent<Void>()

    val loggedInCommand = SingleLiveEvent<Void>()
    val cancelledCommand = SingleLiveEvent<Void>()

    private val mAuth = FirebaseAuth.getInstance()

    fun handleLoginButtonClick() {
        if (username.value.isNullOrBlank() || password.value.isNullOrBlank() || email.value.isNullOrBlank()) {
            validationErrorCommand.call()
            return
        }

        isLoading.value = true

        signUp()

    }

    fun handleCancel() {
        cancelledCommand.call()
    }

    private fun signUp(){
        mAuth.createUserWithEmailAndPassword(email.value.toString(), password.value.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val user = User()
                    user.email = email.value.toString()
                    user.name = username.value.toString()

                    FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user)
                        .addOnCompleteListener{ databaseTask ->
                            if (databaseTask.isSuccessful){
                                isLoading.value = false
                                loggedInCommand.call()
                            }
                        }
                } else {
                    // If sign in fails, display a message to the user.
                    validationErrorCommand.call()
                }
            }
    }
}