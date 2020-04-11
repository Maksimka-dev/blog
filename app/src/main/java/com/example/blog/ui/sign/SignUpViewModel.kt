package com.example.blog.ui.sign

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class SignUpViewModel : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()
    var activity: Activity? = null
    var context: Context? = null

    val liveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun signUp(){

        val email = activity!!.findViewById<EditText>(R.id.emailETSignUp).text.toString()
        val name = activity!!.findViewById<EditText>(R.id.nameET).text.toString()
        val password = activity!!.findViewById<EditText>(R.id.passwordET).text.toString()

        Log.d("Hello", email)
        Log.d("Success", password)

        if (email.length >= 6 && password.length >= 6) {
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity!!) { task ->

                    Log.d("TASK", task.isSuccessful.toString())

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("CREATE USER", "createUserWithEmail:success")
                        liveData.value = task.isSuccessful

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("CREATE USER", "createUserWithEmail:failure", task.exception)
                        liveData.value = task.isSuccessful
                    }

                    // ...
                }
        }
    }
}