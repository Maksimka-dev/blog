package com.example.blog.model

import androidx.lifecycle.MutableLiveData
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class BlogRepository {
    val blogsList: MutableLiveData<MutableList<Blog>> =
        mutableLiveData(mutableListOf())
    val blogsAvatarsList: MutableLiveData<MutableList<String>> =
        mutableLiveData()

    companion object {
        private const val BLOGS = "Blogs"
        private const val AVATAR = "avatar.png"
    }

    fun getBlogs(user: User) {
        val databaseReference = FirebaseDatabase.getInstance().getReference(BLOGS)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list: MutableList<Blog> = mutableListOf()
                for (blogSnapshot in snapshot.children) {
                    blogSnapshot.getValue(Blog::class.java)?.let {
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

    fun getBlogsAvatars(user: User, i: Int = 0, list: MutableList<String> = mutableListOf()) {
        if (list.size == user.subbs.size) {
            blogsAvatarsList.value = list
            return
        } else {
            val blog = blogsList.value?.get(i)
            blog?.let {
                if (user.subbs.contains(blog.blogId)) {
                    FirebaseStorage.getInstance().reference.child(BLOGS).child(blog.title)
                        .child(AVATAR)
                        .downloadUrl.addOnSuccessListener {
                            list.add(it.toString())

                            getBlogsAvatars(user, i + 1, list)
                        }
                }
            }
        }
    }
}
