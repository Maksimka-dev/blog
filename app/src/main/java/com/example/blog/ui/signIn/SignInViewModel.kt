package com.example.blog.ui.signIn

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.R
import com.google.firebase.auth.FirebaseAuth


class SignInViewModel : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()
    var activity: Activity? = null
    var context: Context? = null

    val liveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun signIn(){
        val email = activity!!.findViewById<EditText>(R.id.emailET).text.toString()
        val password = activity!!.findViewById<EditText>(R.id.passwordET_).text.toString()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity!!) { task ->

                Log.d("TASK", task.isSuccessful.toString())

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("SIGN USER", "signInWithEmail:success")




                    Toast.makeText(context, "You successfully logged in", Toast.LENGTH_SHORT).show()
                    liveData.value = task.isSuccessful
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("SIGN USER", "signInWithEmail:success", task.exception)
                    liveData.value = task.isSuccessful
                }

                // ...
            }
    }
}