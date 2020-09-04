package com.example.blog.model.blog

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import com.example.blog.model.user.User
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class BlogRepository @Inject constructor(val database: FirebaseDatabase, val storage: FirebaseStorage) {
    val blogsList: MutableLiveData<MutableList<Blog>> =
        mutableLiveData(mutableListOf())
    val blogsAvatarsList: MutableLiveData<MutableList<String>> =
        mutableLiveData()
    val subscription: MutableLiveData<Boolean> =
        mutableLiveData()
    val createdBlog: MutableLiveData<Blog> =
        mutableLiveData()

    companion object {
        private const val BLOGS = "Blogs"
        private const val AVATAR = "avatar.png"
    }

    fun getBlogs(user: User, searchKeyword: String = "") {
        database.getReference(BLOGS).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<Blog> = mutableListOf()
                for (blogSnapshot in snapshot.children) {
                    blogSnapshot.getValue(Blog::class.java)?.let {
                        if (it.title == searchKeyword) {
                            list.add(it)

                            blogsList.value = list
                            return
                        }

                        if (user.subbs.contains(it.blogId)) {
                            list.add(it)
                        }
                    }
                }
                blogsList.value = list
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    fun getBlogsAvatars(
        user: User,
        searchKeyword: String = "",
        i: Int = 0,
        list: MutableList<String> = mutableListOf()
    ) {
        if (list.size == user.subbs.size && user.subbs.size > 0) {
            blogsAvatarsList.value = list
            return
        } else {
            val blog = blogsList.value?.get(i)
            blog?.let {
                if (blog.title == searchKeyword) {
                    storage.reference.child(BLOGS).child(blog.title)
                        .child(AVATAR)
                        .downloadUrl.addOnSuccessListener {
                            list.add(it.toString())
                            blogsAvatarsList.value = list
                        }
                    return
                }

                if (user.subbs.contains(blog.blogId)) {
                    storage.reference.child(BLOGS).child(blog.title)
                        .child(AVATAR)
                        .downloadUrl.addOnSuccessListener {
                            list.add(it.toString())

                            getBlogsAvatars(user, i = i + 1, list = list)
                        }
                }
            }
        }
    }

    fun createBlog(blog: Blog, bitmap: Bitmap) {
        database
            .getReference("Blogs")
            .child(blog.title)
            .setValue(blog)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    val baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.PNG, 20, baos)
                    val data = baos.toByteArray()

                    storage
                        .reference
                        .child("Blogs")
                        .child(blog.title)
                        .child("avatar.png")
                        .putBytes(data)
                        .addOnSuccessListener {
                            createdBlog.value = blog
                        }
                }
            }
    }

    fun subscribe(user: User, blog: Blog) {
        if (!user.subbs.contains(blog.blogId)) {
            user.subbs.add(blog.blogId)
            database
                .getReference("Users/${FirebaseAuth.getInstance().currentUser!!.uid}/subbs")
                .setValue(user.subbs)
                .addOnCompleteListener {
                    subscription.value = true
                }
        }
    }
}
