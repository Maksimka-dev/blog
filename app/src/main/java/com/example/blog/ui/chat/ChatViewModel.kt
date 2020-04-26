@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.R
import com.example.blog.blog.Blog
import com.example.blog.livedata.SingleLiveEvent
import com.example.blog.livedata.mutableLiveData
import com.example.blog.view.MAX_DOWNLOAD_SIZE_BYTES
import com.example.blog.view.MAX_MESSAGE_LENGTH
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class ChatViewModel : ViewModel() {

    val items: MutableLiveData<Triple<List<String>, List<Bitmap?>, List<String>>> = mutableLiveData()

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    var blog: Blog = Blog()

    val messageCommand = SingleLiveEvent<Void>()
    val timeCommand = SingleLiveEvent<Void>()
    val imageCommand = SingleLiveEvent<Void>()

    fun setUp(){
        items.value = Triple(messages as List<String>, images as List<Bitmap?>, time as List<String>)
    }

    var activity: Activity? = null
    var context: Context? = null
    var bundle: Bundle? = null

    var messages: ArrayList<String> = arrayListOf()
    var time: ArrayList<String> = arrayListOf()
    var images: ArrayList<Bitmap?> = arrayListOf(null)

    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    var bitmapImage: Bitmap? = null


    fun getTime() {
        val ref: DatabaseReference = database.getReference("Blogs")
        ref.child("${blog.title}/time")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    time.clear()
                    for (chatSnapshot: DataSnapshot in dataSnapshot.children) {
                        val time: String? = chatSnapshot.getValue(String()::class.java)
                        this@ChatViewModel.time.add(time!!)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        timeCommand.call()
    }

    fun getMessages() {
        blog.title = bundle!!.getString("title", "")
        blog.blogId = bundle!!.getString("blogId", "")
        blog.ownerId = bundle!!.getString("ownerId", "")

        val ref: DatabaseReference = database.getReference("Blogs")
        ref.child("${blog.title}/messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    messages.clear()
                    for (chatSnapshot: DataSnapshot in dataSnapshot.children) {
                        val message: String? = chatSnapshot.getValue(String()::class.java)
                        messages.add(message!!)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        messageCommand.call()
    }

    fun getImages(){
        images = arrayListOf()
        for (i in 0..messages.size){
            images.add(null)
        }

        for (i in 0..messages.size) {
            val imageRef = storage.reference
                .child("Blogs")
                .child(blog.title)
                .child("$i.png")

            imageRef.getBytes(MAX_DOWNLOAD_SIZE_BYTES)
                .addOnSuccessListener {
                    images[i] = BitmapFactory.decodeByteArray(it, 0, it.size)
                }
        }
        imageCommand.call()
    }

    fun onSendBtnClick(){
        if (isNetworkConnected()) {
            if (user!!.uid == blog.ownerId) {

                uploadMessage()

            } else Toast.makeText(context, "You are not an admin of this blog", Toast.LENGTH_SHORT).show()

        } else displayNoConnection()
    }

    private fun uploadMessage() {
        val textField = activity!!.findViewById<EditText>(R.id.message)
        if (textField.text.length in 1..MAX_MESSAGE_LENGTH) {

            val messageRef = database.getReference("Blogs")
            messageRef.child("${blog.title}/messages")
                .setValue(messages)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        messages.add(textField.text.toString())

                        val date = Date()
                        val dateFormat = SimpleDateFormat("MM.dd hh:mm", Locale.getDefault())
                        time.add(dateFormat.format(date))
                        messageRef.child("${blog.title}/time")
                            .setValue(time).addOnSuccessListener {
                                uploadPicture()
                            }
                    }
                }
            textField.setText("")
        }
    }

    private fun uploadPicture() {
        if (bitmapImage == null) {
            bitmapImage = BitmapFactory.decodeResource(context!!.resources, R.mipmap.tiny)
        }

        images.add(bitmapImage)

        val baos = ByteArrayOutputStream()
        bitmapImage!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val data = baos.toByteArray()

        FirebaseStorage
            .getInstance()
            .getReference("Blogs")
            .child("${blog.title}/${messages.size - 1}.png")
            .putBytes(data)
            .addOnFailureListener {
                bitmapImage = null
            }
            .addOnSuccessListener {
                items.value =
                    Triple(
                        messages as List<String>,
                        images as List<Bitmap?>,
                        time as List<String>
                    )
                getImages()
                bitmapImage = null
            }
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