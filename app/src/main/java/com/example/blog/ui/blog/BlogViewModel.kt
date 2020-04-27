@file:Suppress("DEPRECATION")

package com.example.blog.ui.blog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.example.blog.util.user.User
import com.example.blog.util.view.MAX_DOWNLOAD_SIZE_BYTES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class BlogViewModel : ViewModel() {

    val items: MutableLiveData<Pair<List<Blog>, List<Bitmap?>>> =
        mutableLiveData()

    val progressVisibility: MutableLiveData<Int> =
        mutableLiveData(View.VISIBLE)

    val isCreateDialogOpen =
        mutableLiveData(false)

    private val mAuth = FirebaseAuth.getInstance()

    private var user: User? =
        User()
    private val firebaseUser = mAuth.currentUser

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    var blog = Blog()

    val internetCommand = SingleLiveEvent<Void>()
    var isInternetAvailable = false

    val displayInternetCommand =
        SingleLiveEvent<Void>()

    val openCommand = SingleLiveEvent<Void>()

    private var avatarList: ArrayList<Bitmap?> = arrayListOf()
    private var blogArrayList: ArrayList<Blog> = arrayListOf()

    private var blogSnap: DataSnapshot? = null
    private var userSnap: DataSnapshot? = null

    private val databaseRef: DatabaseReference = database.getReference("Blogs")
    private val storageReference = FirebaseStorage.getInstance().reference.child("Blogs")

    fun generateItems(){
        if (isNetworkConnected()) {
            progressVisibility.value = View.VISIBLE

            if (firebaseUser != null) {
                getUser(firebaseUser)
            }
        } else displayNoConnection()
    }

    private fun getBlogs() {
        blogArrayList.clear()
        avatarList.clear()

        items.value = Pair(
            blogArrayList as List<Blog>,
            avatarList as List<Bitmap?>
        )

        for (blogSnapshot in blogSnap!!.children) {
            val blog = blogSnapshot.getValue(Blog::class.java)

            if (blog != null && user?.subbs?.contains(blog.blogId)!!) {
                storageReference.child(blog.title)
                    .child("avatar.png")
                    .getBytes(MAX_DOWNLOAD_SIZE_BYTES)
                    .addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                        avatarList.add(bitmap)
                        blogArrayList.add(blog)

                        if (user?.subbs?.size == avatarList.size) {
                            items.value = Pair(
                                blogArrayList as List<Blog>,
                                avatarList as List<Bitmap?>
                            )
                            progressVisibility.value = View.INVISIBLE
                        }
                    }
            }
        }
    }

    private fun getUser(firebaseUser: FirebaseUser) {
        if (userSnap == null) {
            val userRef = FirebaseDatabase.getInstance()
                .getReference("Users/${firebaseUser.uid}")
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userSnap = dataSnapshot
                    user = userSnap!!.getValue(User::class.java)
                    onBlogSnap()
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        } else {
            user = userSnap!!.getValue(User::class.java)
            onBlogSnap()
        }
    }

    private fun onBlogSnap() {
        if (blogSnap == null) {
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    blogSnap = dataSnapshot
                    getBlogs()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } else getBlogs()
    }

    fun handleOpenCLick(currentBlog: Blog){
        blog = currentBlog
        openCommand.call()
    }

    fun handleSuccessfulCreate(){
        isCreateDialogOpen.value = false
        generateItems()
    }

    fun handleCreateButtonClick(){
        isCreateDialogOpen.value = true
    }

    private fun displayNoConnection(){
        displayInternetCommand.call()
    }

    private fun isNetworkConnected(): Boolean {
        internetCommand.call()
        return isInternetAvailable
    }
}