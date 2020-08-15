package com.example.blog.ui.findblog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.model.User
import com.example.blog.model.Blog
import com.example.blog.util.livedata.SingleLiveEvent
import com.example.blog.util.livedata.mutableLiveData
import com.example.blog.util.view.MAX_DOWNLOAD_SIZE_BYTES
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class FindBlogViewModel : ViewModel() {
    val items: MutableLiveData<Pair<List<Blog>, List<Bitmap?>>> =
        mutableLiveData()

    val progressVisibility: MutableLiveData<Int> =
        mutableLiveData(View.INVISIBLE)
    val subscribeVisibility: MutableLiveData<Int> =
        mutableLiveData(View.INVISIBLE)

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    var blog = Blog()
    private val mAuth = FirebaseAuth.getInstance()
    private val firebaseUser = mAuth.currentUser
    private var user: User? = null

    val search = mutableLiveData("")

    private var avatarList: ArrayList<Bitmap?> = arrayListOf()
    private var blogArrayList: ArrayList<Blog> = arrayListOf()

    val searchCommand = SingleLiveEvent<Void>()
    val subCommand = SingleLiveEvent<Void>()

    private var dataSnap: DataSnapshot? = null
    private var userSnap: DataSnapshot? = null

    private val databaseRef: DatabaseReference = database.getReference("Blogs")
    private val storageReference = FirebaseStorage.getInstance().reference.child("Blogs")

    fun generateItems() {
        progressVisibility.value = View.VISIBLE

        if (firebaseUser != null) {
            getUser()

            if (dataSnap == null) {
                databaseRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnap = dataSnapshot
                        getData()
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            } else getData()
        }
    }

    private fun getUser() {
        if (userSnap == null) {
            val userRef = FirebaseDatabase.getInstance()
                .getReference("Users/${firebaseUser!!.uid}")
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    userSnap = dataSnapshot
                    user = dataSnapshot.getValue(User::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        } else user = userSnap!!.getValue(User::class.java)
    }

    private fun getData() {
        blogArrayList.clear()
        avatarList.clear()

        for (blogSnapshot in dataSnap!!.children) {
            val blog = blogSnapshot.getValue(Blog::class.java)

            if (blog != null && blog.title == search.value.toString()) {
                storageReference.child(blog.title)
                    .child("avatar.png")
                    .getBytes(MAX_DOWNLOAD_SIZE_BYTES)
                    .addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                        avatarList.add(bitmap)
                        blogArrayList.add(blog)

                        items.value = Pair(blogArrayList as List<Blog>, avatarList as List<Bitmap?>)
                        progressVisibility.value = View.INVISIBLE
                    }
            }
        }
    }

    fun handleSearchClick() {
        searchCommand.call()
    }

    fun handleBlogClick(blog: Blog) {
        subscribeVisibility.value = View.VISIBLE
        this.blog = blog
    }

    fun handleSubClick() {
        subCommand.call()
    }

    fun subscribe() {
        progressVisibility.value = View.VISIBLE
        if (user?.subbs?.contains(blog.blogId) == false) user?.subbs?.add(blog.blogId)
        FirebaseDatabase.getInstance()
            .getReference("Users/${FirebaseAuth.getInstance().currentUser!!.uid}/subbs")
            .setValue(user!!.subbs)
            .addOnCompleteListener {
                progressVisibility.value = View.INVISIBLE
                subscribeVisibility.value = View.INVISIBLE
            }
    }
}
