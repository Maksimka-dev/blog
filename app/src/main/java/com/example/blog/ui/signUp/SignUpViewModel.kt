package com.example.blog.ui.signUp

import android.app.Activity
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.R
import com.example.blog.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


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

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && password.length >= 6 && name.length >= 4) {
            //TODO add progress bar
            mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity!!) { task ->

                    Log.d("TASK", task.isSuccessful.toString())

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("CREATE USER", "createUserWithEmail:success")

                        val source = "abcdefghijklmnopqrstuvwxyz1234567890"
                        val id = (1..25)
                            .map { source.random() }
                            .joinToString("")

                        val user: User = User(name, email, id)

                        FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .setValue(user)
                            .addOnCompleteListener(activity!!){ databaseTask ->
                                if (databaseTask.isSuccessful){
                                    Toast.makeText(context, "You successfully registered", Toast.LENGTH_SHORT).show()
                                    liveData.value = databaseTask.isSuccessful
                                }
                            }
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