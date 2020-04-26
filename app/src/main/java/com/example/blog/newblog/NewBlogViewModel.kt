package com.example.blog.newblog

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.blog.Blog
import com.example.blog.livedata.SingleLiveEvent
import com.example.blog.livedata.mutableLiveData
import com.example.blog.user.User
import com.example.blog.view.ID_LENGTH
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class NewBlogViewModel : ViewModel() {
    var title: MutableLiveData<String> = mutableLiveData(null)
    var description: MutableLiveData<String> = mutableLiveData(null)

    val validationErrorCommand = SingleLiveEvent<Void>()

    val createCommand = SingleLiveEvent<Void>()
    val cancelledCommand = SingleLiveEvent<Void>()

    var blog = Blog()

    val visibility: MutableLiveData<Int> = mutableLiveData(View.INVISIBLE)

    private val mAuth = FirebaseAuth.getInstance()

    var bitmapImage: Bitmap? = null

    fun handleCreateButtonClick() {
        if (title.value.isNullOrBlank()) {
            validationErrorCommand.call()
            return
        }
        visibility.value = View.VISIBLE

        createBlog()
    }

    fun handleCancel() {
        cancelledCommand.call()
    }


    private fun createBlog() {
        blog.title = title.value.toString()
        blog.description = description.value.toString()
        blog.ownerId = mAuth.currentUser!!.uid

        val source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
        blog.blogId = (1..ID_LENGTH)
            .map { source.random() }
            .joinToString("")

        FirebaseDatabase.getInstance()
            .getReference("Blogs")
            .child(blog.title)
            .setValue(blog)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val baos = ByteArrayOutputStream()
                    bitmapImage!!.compress(Bitmap.CompressFormat.PNG, 20, baos)
                    val data = baos.toByteArray()

                    FirebaseStorage.getInstance()
                        .reference
                        .child("Blogs")
                        .child(blog.title)
                        .child("avatar.png")
                        .putBytes(data)
                        .addOnSuccessListener {
                            subscribe()
                        }
                }
            }
    }

    private fun subscribe() {
        var user: User?
        val userRef = FirebaseDatabase.getInstance()
            .getReference("Users/${FirebaseAuth.getInstance().currentUser!!.uid}")

        userRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue(User::class.java)

                if (!user?.subbs?.contains(blog.blogId)!!) {
                    user?.subbs?.add(blog.blogId)

                    FirebaseDatabase.getInstance()
                        .getReference("Users/${FirebaseAuth.getInstance().currentUser!!.uid}/subbs")
                        .setValue(user!!.subbs)
                        .addOnCompleteListener {
                            visibility.value = View.INVISIBLE
                            createCommand.call()
                        }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}