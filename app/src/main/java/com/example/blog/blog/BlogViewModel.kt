@file:Suppress("DEPRECATION")

package com.example.blog.blog

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.livedata.SingleLiveEvent
import com.example.blog.livedata.mutableLiveData
import com.example.blog.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class BlogViewModel : ViewModel() {

    val items: MutableLiveData<Pair<List<Blog>, List<Bitmap?>>> = mutableLiveData()

    val progressVisibility: MutableLiveData<Int> = mutableLiveData(View.VISIBLE)

    private val mAuth = FirebaseAuth.getInstance()

    private var user: User? = User()
    private val firebaseUser = mAuth.currentUser

    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    var avatarList: ArrayList<Bitmap?> = arrayListOf()
    var blogArrayList: ArrayList<Blog> = arrayListOf()

    val loadCommand = SingleLiveEvent<Void>()

    fun handleFindClick() {
        //items.value = items.value!! + generateItems()
    }

    fun handleOpenCLick(blog: Blog){

    }

    fun generateItems(){
        progressVisibility.value = View.VISIBLE

        if (firebaseUser != null) {
            val userRef = FirebaseDatabase.getInstance()
                .getReference("Users/${firebaseUser.uid}")
            userRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    user = dataSnapshot.getValue(User::class.java)
                    Log.w("GENERATEITEMS", "${user.toString()}___________")

                    val databaseRef: DatabaseReference = database.getReference("Blogs")
                    val maxDownloadSizeBytes: Long = 5000 * 5000
                    val storageReference = FirebaseStorage.getInstance()
                        .reference
                        .child("Blogs")

                    databaseRef.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            blogArrayList.clear()
                            avatarList.clear()

                            for (blogSnapshot in dataSnapshot.children) {
                                val blog = blogSnapshot.getValue(Blog::class.java)

                                if (blog != null && user?.subbs?.contains(blog.blogId)!!) {
                                    storageReference.child(blog.title)
                                        .child("avatar.png")
                                        .getBytes(maxDownloadSizeBytes)
                                        .addOnSuccessListener {
                                            val bitmap = BitmapFactory.decodeByteArray(it, 0, it.size)
                                            avatarList.add(bitmap)
                                            blogArrayList.add(blog)

                                            if (user?.subbs?.size == avatarList.size) {
                                                items.value = Pair(blogArrayList as List<Blog>, avatarList as List<Bitmap?>)
                                                loadCommand.call()
                                                progressVisibility.value = View.INVISIBLE
                                                Log.d("Add", "____________________")
                                            }
                                        }
                                }
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }




    private fun displayNameTooShort(){
        //Toast.makeText(context, "Title is too short", Toast.LENGTH_SHORT).show()
    }

    private fun displayNoAvatar(){
        //Toast.makeText(context, "Select blog's icon first", Toast.LENGTH_SHORT).show()
    }

    private fun displayNoConnection(){
        //Toast.makeText(context, "No internet connection available", Toast.LENGTH_SHORT).show()
    }

    //private fun isNetworkConnected(): Boolean {
    //    val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    //    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    //    return activeNetwork?.isConnectedOrConnecting == true
    //}
}