package com.example.blog.model

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class ChatRepository {
    private val messagesList: MutableList<String> = mutableListOf()
    private val timesList: MutableList<String> = mutableListOf()
    private val picsUrlsList: MutableList<String> = mutableListOf()

    val chat: MutableLiveData<Chat> = mutableLiveData(Chat())
    var blog: Blog = Blog()

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Blogs")
    private val storageReference: StorageReference =
        FirebaseStorage.getInstance().reference.child("Blogs")

    fun getMessages() {
        databaseReference.child("${blog.title}/messages")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    messagesList.clear()
                    for (chatSnapshot: DataSnapshot in dataSnapshot.children) {
                        chatSnapshot.getValue(String()::class.java)?.let { messagesList.add(it) }
                    }
                    getTimes()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun getTimes() {
        databaseReference.child("${blog.title}/time")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    timesList.clear()
                    for (chatSnapshot: DataSnapshot in dataSnapshot.children) {
                        chatSnapshot.getValue(String()::class.java)?.let { timesList.add(it) }
                    }
                    picsUrlsList.clear()
                    getPicsUrls()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    private fun getPicsUrls(i: Int = 0) {
        if (picsUrlsList.size == messagesList.size) {
            setChat()
            return
        } else {
            storageReference
                .child(blog.title)
                .child("$i.png")
                .downloadUrl.addOnSuccessListener {
                    picsUrlsList.add(it.toString())

                    getPicsUrls(i + 1)
                }
        }
    }

    private fun setChat() {
        val tempChat = Chat().apply {
            this.messages.addAll(messagesList)
            this.times.addAll(timesList)
            this.picsUrls.addAll(picsUrlsList)
        }
        chat.value = tempChat
    }

    fun sendMessage(message: Message, blog: Blog) {
        messagesList.add(message.text)
        timesList.add(message.time)
        // upload message
        databaseReference.child("${blog.title}/messages")
            .setValue(messagesList)

        // upload time
        databaseReference.child("${blog.title}/time")
            .setValue(timesList)

        val baos = ByteArrayOutputStream()

        // upload picture
        message.pic?.let {
            it.compress(Bitmap.CompressFormat.PNG, 20, baos)
            val data = baos.toByteArray()

            FirebaseStorage
                .getInstance()
                .getReference("Blogs")
                .child("${blog.title}/${messagesList.size - 1}.png")
                .putBytes(data)
                .addOnSuccessListener {
                    // update chat from database
                    getMessages()
                }
        }
    }
}
