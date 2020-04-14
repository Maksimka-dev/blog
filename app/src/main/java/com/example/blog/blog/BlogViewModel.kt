@file:Suppress("DEPRECATION")

package com.example.blog.blog

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class BlogViewModel : ViewModel() {
    var activity: Activity? = null
    var context: Context? = null

    var userId: String? = null
    var blog: Blog = Blog(userId)

    var title: String = ""
    var description: String = ""
    var avatar: Int = 0

    val changeFragmentLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun createBlog(){
        if (isNetworkConnected()) {
            blog.title = title
            blog.description = description
            blog.avatar = avatar
            blog.ownerId = userId
            blog.time = "10:55"

            FirebaseDatabase.getInstance()

            FirebaseDatabase.getInstance()
                .getReference("Blogs")
                .child(blog.title)
                .setValue(blog)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        changeFragmentLiveData.value = true
                    }
                }

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