@file:Suppress("DEPRECATION")

package com.example.blog.ui.home

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.blog.Blog
import com.example.blog.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage


class HomeViewModel : ViewModel() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    var activity: Activity? = null
    var context: Context? = null

    val blogLiveData: MutableLiveData<Boolean> = MutableLiveData()

    var avatarList: ArrayList<Bitmap?> = arrayListOf()
    var blogArrayList: ArrayList<Blog> = arrayListOf()

    val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()

    fun init(){
        mAuth = FirebaseAuth.getInstance()
        val firebaseUser = mAuth.currentUser

        //Получаем пользователя из бд чтобы проверить есть ли канал в его подписках
        var user: User? = User()

        if (firebaseUser != null) {
            val userRef = FirebaseDatabase.getInstance()
                .getReference("Users/${firebaseUser.uid}")

            userRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    user = dataSnapshot.getValue(User::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("VALUE", "Failed to read value.", error.toException())
                }
            })

            if (isNetworkConnected()) {
                val myRef: DatabaseReference = database.getReference("Blogs")
                val maxDownloadSizeBytes: Long = 5120 * 5120
                val storageReference = FirebaseStorage.getInstance()
                    .reference
                    .child("Blogs")

                // Read from the database
                myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.

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

                                        Log.d("SIZES", "${user?.subbs?.size} ${avatarList.size}")
                                        if (user?.subbs?.size == avatarList.size) {
                                            blogLiveData.value = true
                                        }
                                    }
                            }
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        // Failed to read value
                        Log.w("VALUE", "Failed to read value.", error.toException())
                    }
                })

                userLiveData.value = firebaseUser

            } else displayNoConnection()
        } else displayLoginRequired()
    }


    private fun displayLoginRequired(){
        Toast.makeText(context, "You need to login first", Toast.LENGTH_SHORT).show()
    }

    private fun displayNoConnection(){
        Toast.makeText(context, "No internet connection available", Toast.LENGTH_SHORT).show()
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun onRefreshButtonClick(){
        Toast.makeText(context, "Refreshing...", Toast.LENGTH_SHORT).show()
        init()
    }
}