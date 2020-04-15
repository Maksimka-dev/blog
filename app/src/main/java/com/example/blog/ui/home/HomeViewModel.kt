@file:Suppress("DEPRECATION")

package com.example.blog.ui.home

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.blog.Blog
import com.example.blog.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class HomeViewModel : ViewModel() {
    private var mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var database: FirebaseDatabase = FirebaseDatabase.getInstance()

    var activity: Activity? = null
    var context: Context? = null

    val blogLiveData: MutableLiveData<Boolean> = MutableLiveData()
    var blogArrayList: ArrayList<Blog> = arrayListOf()

    val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()

    fun init(){
        mAuth = FirebaseAuth.getInstance()
        val firebaseUser = mAuth.currentUser
        Toast.makeText(context, firebaseUser.toString(), Toast.LENGTH_LONG).show()

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

                // Read from the database
                myRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.

                        blogArrayList.clear()

                        for (artistSnapshot in dataSnapshot.children) {
                            val blog = artistSnapshot.getValue(Blog::class.java)

                            if (blog != null) {
                                Log.d("BLOG", blog.title)
                                if (user?.subbs?.contains(blog.blogId)!!) {
                                    blogArrayList.add(blog)
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
                //По окончании загрузки
                blogLiveData.value = true

            } else displayNoConnection()
        } else displayLoginRequired()
    }

    private fun displayLoginRequired(){
        Toast.makeText(context, "You need to login first", Toast.LENGTH_LONG).show()
    }

    private fun displayNoConnection(){
        Toast.makeText(context, "No internet connection available", Toast.LENGTH_LONG).show()
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun onRefreshButtonClick(){
        init()
    }
}