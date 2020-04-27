@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.blog.Blog
import com.example.blog.livedata.SingleLiveEvent
import com.example.blog.livedata.mutableLiveData
import com.example.blog.view.MAX_DOWNLOAD_SIZE_BYTES
import com.example.blog.view.MAX_MESSAGE_LENGTH
import com.google.firebase.auth.FirebaseAuth
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

    val internetCommand = SingleLiveEvent<Void>()
    var isInternetAvailable = false

    val displayInternetCommand = SingleLiveEvent<Void>()
    val displayAdminCommand = SingleLiveEvent<Void>()

    fun setUp(){
        items.value = Triple(messages as List<String>, images as List<Bitmap?>, times as List<String>)
    }

    private val user = FirebaseAuth.getInstance().currentUser

    var messages: ArrayList<String> = arrayListOf()
    var times: ArrayList<String> = arrayListOf()
    private var images: ArrayList<Bitmap?> = arrayListOf()

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

        for (i in 0 until messages.size){
            images.add(null)
        }

        for (i in 0 until messages.size) {
            val imageRef = storage.reference
                .child("Blogs")
                .child(blog.title)
                .child("$i.png")

            imageRef.getBytes(MAX_DOWNLOAD_SIZE_BYTES)
                .addOnSuccessListener {
                    images[i] = BitmapFactory.decodeByteArray(it, 0, it.size)
                    //images.add(BitmapFactory.decodeByteArray(it, 0, it.size))
                    if (!images.contains(null)) {
                        imageCommand.call()
                    }
                }
        }

    }

    fun onSendBtnClick(){
        if (isNetworkConnected()) {
            if (user!!.uid == blog.ownerId) {

                val date = Date()
                val dateFormat = SimpleDateFormat("MM.dd hh:mm", Locale.getDefault())
                if (bitmapImage == null) {
                    bitmapImage = defaultBitmap
                }

                images.add(bitmapImage!!)
                times.add(dateFormat.format(date))
                messages.add(textField.value.toString())

                imageCommand.call()

                uploadMessage()
                uploadPicture()

            } else displayNotAdmin()
        } else displayNoConnection()
    }

    private fun uploadMessage() {
        if (textField.value!!.length in 0..MAX_MESSAGE_LENGTH) {
            val messageRef = database.getReference("Blogs")
            messageRef.child("${blog.title}/messages")
                .setValue(messages)

            messageRef.child("${blog.title}/time")
                .setValue(times)
            textField.value = ""
        }
    }

    private fun uploadPicture() {
        val baos = ByteArrayOutputStream()
        bitmapImage!!.compress(Bitmap.CompressFormat.PNG, 20, baos)
        val data = baos.toByteArray()

        FirebaseStorage
            .getInstance()
            .getReference("Blogs")
            .child("${blog.title}/${messages.size - 1}.png")
            .putBytes(data)

        bitmapImage = null
    }

    private fun displayNotAdmin(){
        displayAdminCommand.call()
    }

    private fun displayNoConnection(){
        displayInternetCommand.call()
    }

    private fun isNetworkConnected(): Boolean {
        internetCommand.call()
        return isInternetAvailable
    }
}