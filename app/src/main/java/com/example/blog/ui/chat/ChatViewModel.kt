@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.R
import com.example.blog.blog.Blog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatViewModel : ViewModel() {
    var activity: Activity? = null
    var context: Context? = null
    var bundle: Bundle? = null

    private val database = FirebaseDatabase.getInstance()

    var lastPos = 0

    var blog: Blog = Blog()
    var title: String = ""

    var messagesArrayList: ArrayList<String> = arrayListOf()

    private var user = FirebaseAuth.getInstance().currentUser

    val chatLiveData: MutableLiveData<Boolean> = MutableLiveData()

    var bitmapImage: Bitmap? = null

    fun createView(){
        blog.title = bundle!!.getString("title", "")
        blog.blogId = bundle!!.getString("blogId", "")
        blog.messages = bundle!!.getStringArrayList("messages")!!
        blog.ownerId = bundle!!.getString("ownerId", "")

        messagesArrayList = blog.messages
        lastPos = messagesArrayList.lastIndex

        val toolbar: androidx.appcompat.widget.Toolbar = activity!!.findViewById(R.id.toolbar)
        toolbar.title = blog.title

        val ref = database.getReference("Blogs")
        ref.child("${blog.title}/messages")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    messagesArrayList.clear()

                    for (chatSnapshot in dataSnapshot.children) {
                        val message = chatSnapshot.getValue(String()::class.java)

                        messagesArrayList.add(message!!)
                        lastPos = messagesArrayList.lastIndex
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("VALUE", "Failed to read value.", error.toException())
                }
            })

        chatLiveData.value = true
    }

    fun onSendBtnClick(){
        if (isNetworkConnected()) {

            if (user!!.uid == blog.ownerId) {

                val textField = activity!!.findViewById<EditText>(R.id.message)

                if (textField.text.length > 1) {
                    messagesArrayList.add(textField.text.toString())

                    val ref = database.getReference("Blogs")
                    ref.child("${blog.title}/messages")
                        .setValue(messagesArrayList)
                        .addOnCompleteListener(activity!!) { task ->
                            if (task.isSuccessful) {
                                chatLiveData.value = true
                            }
                        }
                    textField.setText("")
                }

            } else Toast.makeText(context, "You are not an admin of this blog", Toast.LENGTH_SHORT).show()

        } else displayNoConnection()
    }


    fun onClipBtnClick(){
        val imageField: ImageView = activity!!.findViewById(R.id.messagePic)

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