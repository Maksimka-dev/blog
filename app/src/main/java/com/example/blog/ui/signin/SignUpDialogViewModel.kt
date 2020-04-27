package com.example.blog.ui.signin

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.example.blog.util.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class SignUpDialogViewModel : ViewModel() {
    val username = mutableLiveData("")
    val password = mutableLiveData("")
    val email = mutableLiveData("")

    val remember = mutableLiveData(true)

    val isLoading: MutableLiveData<Int> =
        mutableLiveData(View.INVISIBLE)

    val validationErrorCommand =
        SingleLiveEvent<Void>()

    val loggedInCommand = SingleLiveEvent<Void>()
    val cancelledCommand = SingleLiveEvent<Void>()

    private val mAuth = FirebaseAuth.getInstance()

    fun handleLoginButtonClick() {
        if (username.value.isNullOrBlank() || password.value.isNullOrBlank() || email.value.isNullOrBlank()) {
            validationErrorCommand.call()
            return
        }

        isLoading.value = View.VISIBLE

        signUp()

    }

    fun handleCancel() {
        cancelledCommand.call()
    }

    private fun signUp(){
        mAuth.createUserWithEmailAndPassword(email.value.toString(), password.value.toString())
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = User()
                    user.email = email.value.toString()
                    user.name = username.value.toString()

                    FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .setValue(user)
                        .addOnCompleteListener{ databaseTask ->
                            if (databaseTask.isSuccessful){
                                isLoading.value = View.INVISIBLE
                                loggedInCommand.call()
                            }
                        }
                } else {
                    validationErrorCommand.call()
                }
            }
    }
}