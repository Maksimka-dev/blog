package com.example.blog.ui.sign

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {
    private val mAuth = FirebaseAuth.getInstance()
    var activity: Activity? = null
    var context: Context? = null

    val liveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
}