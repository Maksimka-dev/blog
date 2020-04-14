@file:Suppress("DEPRECATION")

package com.example.blog.ui.home

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.blog.Blog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class HomeViewModel : ViewModel() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var activity: Activity? = null
    var context: Context? = null

    val blogLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var blogArrayList: ArrayList<Blog> = arrayListOf()

    val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()

    fun init(){
        mAuth = FirebaseAuth.getInstance()
        val user = mAuth.currentUser
        Toast.makeText(context, user.toString(), Toast.LENGTH_LONG).show()

        if (isNetworkConnected()) {
            Log.d("User", user.toString())
            if (user != null) {
                //TODO загрузить каналы пользователя
                userLiveData.value = user
                Log.d("livedata", userLiveData.value.toString())
                //По окончании загрузки
                blogLiveData.value = true

            } else displayLoginRequired()

        } else displayNoConnection()
    }

    private fun displayLoginRequired(){
        Toast.makeText(context, "You need to login first", Toast.LENGTH_LONG).show()
    }

    private fun displayNoConnection(){
        Toast.makeText(context, "No internet connection available", Toast.LENGTH_LONG).show()
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun onRefreshButtonClick(){
        Log.d("Button", "PRESSED_____________")
        init()
    }
}