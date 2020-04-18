@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.R
import com.example.blog.blog.Blog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream


class ChatViewModel : ViewModel() {
    var activity: Activity? = null
    var context: Context? = null
    var bundle: Bundle? = null

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    var lastPos = 0

    var blog: Blog = Blog()
    var title: String = ""

    var messagesArrayList: ArrayList<String> = arrayListOf()
    var picsArrayList: ArrayList<Bitmap?> = arrayListOf(null)

    private var user: FirebaseUser? = FirebaseAuth.getInstance().currentUser

    val chatLiveData: MutableLiveData<Boolean> = MutableLiveData()

    val messageSentLiveData: MutableLiveData<Boolean> = MutableLiveData()

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

        initMessages()

        initPics()

        Log.d("SIZES", "${picsArrayList.size} - ${messagesArrayList.size}")
    }

    private fun initMessages() {
        val ref: DatabaseReference = database.getReference("Blogs")
        ref.child("${blog.title}/messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    messagesArrayList.clear()

                    //добавляем сообщение в массив
                    for (chatSnapshot: DataSnapshot in dataSnapshot.children) {
                        val message: String? = chatSnapshot.getValue(String()::class.java)
                        messagesArrayList.add(message!!)
                    }
                    lastPos = messagesArrayList.lastIndex
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                    Log.w("VALUE", "Failed to read value.", error.toException())
                }
            })
    }

    private fun initPics(){
        setRefreshTimer()
        //добавляем картинку в массив
        val maxDownloadSizeBytes: Long = 5120 * 5120

        picsArrayList = arrayListOf()
        for (i in 0..messagesArrayList.size){
            picsArrayList.add(null)
        }

        for (i in 0..messagesArrayList.size) {
            val imageRef = storage.reference
                .child("Blogs")
                .child(blog.title)
                .child("$i.png")

            imageRef.getBytes(maxDownloadSizeBytes)
                .addOnSuccessListener {
                    picsArrayList[i] = BitmapFactory.decodeByteArray(it, 0, it.size)
                }
        }
    }

    private fun setRefreshTimer(){
        Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT).show()
        object : CountDownTimer(2000, 2000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                //обновить recycler view
                chatLiveData.value = true
            }
        }.start()
    }

    fun onSendBtnClick(){
        setRefreshTimer()
        if (isNetworkConnected()) {

            if (user!!.uid == blog.ownerId) {

                val textField = activity!!.findViewById<EditText>(R.id.message)
                //Отправляем текст
                if (textField.text.length > 1) {
                    messagesArrayList.add(textField.text.toString())

                    val ref = database.getReference("Blogs")
                    ref.child("${blog.title}/messages")
                        .setValue(messagesArrayList)
                        .addOnCompleteListener(activity!!) { task ->
                            if (task.isSuccessful) {
                                initMessages()

                                if (bitmapImage == null){
                                    bitmapImage = BitmapFactory.decodeResource(context!!.resources, R.mipmap.euro)
                                }

                                val baos = ByteArrayOutputStream()
                                bitmapImage!!.compress(Bitmap.CompressFormat.PNG, 100, baos)
                                val data = baos.toByteArray()

                                FirebaseStorage
                                    .getInstance()
                                    .getReference("Blogs")
                                    .child("${blog.title}/${messagesArrayList.size - 1}.png")
                                    .putBytes(data)
                                    .addOnFailureListener {
                                        bitmapImage = null
                                        Toast.makeText(
                                            context,
                                            "Fail uploading image",
                                            Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnSuccessListener {
                                            initPics()
                                            bitmapImage = null
                                            Toast.makeText(
                                                context,
                                                "Upload successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                //обновляем recycler view
                                chatLiveData.value = true
                            }
                        }
                    textField.setText("")
                }

            } else Toast.makeText(context, "You are not an admin of this blog", Toast.LENGTH_SHORT).show()

        } else displayNoConnection()
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