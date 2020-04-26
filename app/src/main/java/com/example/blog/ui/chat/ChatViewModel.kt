@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
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

    val textField = mutableLiveData("")

    var blog: Blog = Blog()

    val imageCommand = SingleLiveEvent<Void>()

    fun setUp(){
        items.value = Triple(messages as List<String>, images as List<Bitmap?>, times as List<String>)
    }

    var context: Context? = null

    var messages: ArrayList<String> = arrayListOf()
    var times: ArrayList<String> = arrayListOf()
    private var images: ArrayList<Bitmap?> = arrayListOf(null)

    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    var bitmapImage: Bitmap? = null
    var defaultBitmap: Bitmap? = null

    var params = Triple("", "", "")


    private fun getTime() {
        val ref: DatabaseReference = database.getReference("Blogs")
        ref.child("${blog.title}/time")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    times.clear()
                    for (chatSnapshot: DataSnapshot in dataSnapshot.children) {
                        val time: String? = chatSnapshot.getValue(String()::class.java)
                        times.add(time!!)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun getMessages() {
        blog.title = params.first
        blog.blogId = params.second
        blog.ownerId = params.third

        val ref: DatabaseReference = database.getReference("Blogs")
        ref.child("${blog.title}/messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    messages.clear()
                    for (chatSnapshot: DataSnapshot in dataSnapshot.children) {
                        val message: String? = chatSnapshot.getValue(String()::class.java)
                        messages.add(message!!)
                    }

                    getTime()
                    getImages()
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun getImages(){
        images.clear()

        for (i in 0 until messages.size) {
            val imageRef = storage.reference
                .child("Blogs")
                .child(blog.title)
                .child("$i.png")

            imageRef.getBytes(MAX_DOWNLOAD_SIZE_BYTES)
                .addOnSuccessListener {
                    images.add(BitmapFactory.decodeByteArray(it, 0, it.size))
                    if (i == messages.size - 1) {
                        imageCommand.call()
                    }
                }
        }

    }

    fun onSendBtnClick(){
        //if (isNetworkConnected()) {
            //if (user!!.uid == blog.ownerId) {

                uploadMessage()

            //} else Toast.makeText(context, "You are not an admin of this blog", Toast.LENGTH_SHORT).show()

        //} else displayNoConnection()
    }

    private fun uploadMessage() {
        if (textField.value!!.length in 1..MAX_MESSAGE_LENGTH) {

            val date = Date()
            val dateFormat = SimpleDateFormat("MM.dd hh:mm", Locale.getDefault())
            messages.add(textField.value.toString())
            
            val messageRef = database.getReference("Blogs")
            messageRef.child("${blog.title}/messages")
                .setValue(messages)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful) {
                        times.add(dateFormat.format(date))
                        messageRef.child("${blog.title}/time")
                            .setValue(times).addOnSuccessListener {
                                uploadPicture()
                            }
                    }
                }
            textField.value = ""
        }
    }

    private fun uploadPicture() {
        if (bitmapImage == null) {
            bitmapImage = defaultBitmap
        }

        images.add(bitmapImage)

        val baos = ByteArrayOutputStream()
        bitmapImage!!.compress(Bitmap.CompressFormat.PNG, 20, baos)
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
                setUp()
                bitmapImage = null
            }
    }


    //private fun displayNoConnection(){
    //    Toast.makeText(context, "No internet connection available", Toast.LENGTH_SHORT).show()
    //}
//
    //private fun isNetworkConnected(): Boolean {
    //    val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    //    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    //    return activeNetwork?.isConnectedOrConnecting == true
    //}
}