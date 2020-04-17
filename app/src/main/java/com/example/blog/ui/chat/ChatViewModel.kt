package com.example.blog.ui.chat

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.R
import com.example.blog.blog.Blog
import com.google.firebase.auth.FirebaseAuth

class ChatViewModel : ViewModel() {
    var activity: Activity? = null
    var context: Context? = null
    var bundle: Bundle? = null

    var blog: Blog = Blog()
    var title: String = ""

    var messagesArrayList: ArrayList<String> = arrayListOf("Hello", "World", "Lorem Ipsum")

    val inputBtnVisible = ObservableField<Boolean>()

    private var user = FirebaseAuth.getInstance().currentUser

    val chatLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun createView(){
        if (user!!.uid == blog.blogId) inputBtnVisible.set(true)
        else inputBtnVisible.set(false)

        blog.title = bundle!!.getString("title", "")
        blog.blogId = bundle!!.getString("blogId", "")
        blog.messages = bundle!!.getStringArrayList("messages")!!

        val toolbar: androidx.appcompat.widget.Toolbar = activity!!.findViewById(R.id.toolbar)
        toolbar.title = blog.title

        chatLiveData.value = true
        Log.d("ARRAYLIST", messagesArrayList[2])
    }

    fun onSendBtnClick(){

    }

    private fun displayNoConnection(){
        Toast.makeText(context, "No internet connection available", Toast.LENGTH_SHORT).show()
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}