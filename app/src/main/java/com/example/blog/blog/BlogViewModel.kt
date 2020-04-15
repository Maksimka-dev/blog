@file:Suppress("DEPRECATION")

package com.example.blog.blog

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.blog.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class BlogViewModel : ViewModel() {
    var activity: Activity? = null
    var context: Context? = null

    var userId: String? = null
    var blog: Blog = Blog()

    var title: String = ""
    var description: String = ""
    var avatar: Int = 0

    val changeFragmentLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun createBlog(){
        if (isNetworkConnected()) {
            blog.title = title
            blog.description = description
            blog.avatar = avatar
            blog.ownerId = userId!!
            blog.time = "10:55"

            val source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
            blog.blogId = (1..25)
                .map { source.random() }
                .joinToString("")

            FirebaseDatabase.getInstance()
                .getReference("Blogs")
                .child(blog.title)
                .setValue(blog)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        changeFragmentLiveData.value = true
                    }
                }

            //Добавляем канал в подписки
            var user: User?

            val userRef = FirebaseDatabase.getInstance()
                .getReference("Users/${FirebaseAuth.getInstance().currentUser!!.uid}")

            userRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    user = dataSnapshot.getValue(User::class.java)

                    if (!user?.subbs?.contains(blog.blogId)!!) {
                        user?.subbs?.add(blog.blogId)
                        Log.d("SUBBS", user!!.subbs[0])

                        //Запись в подписки
                        FirebaseDatabase.getInstance()
                            .getReference("Users/${FirebaseAuth.getInstance().currentUser!!.uid}/subbs")
                            .setValue(user!!.subbs)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w("VALUE", "Failed to read value.", error.toException())
                }
            })

        } else displayNoConnection()
    }

    private fun displayNoConnection(){
        Toast.makeText(context, "No internet connection available", Toast.LENGTH_LONG).show()
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context!!.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }
}