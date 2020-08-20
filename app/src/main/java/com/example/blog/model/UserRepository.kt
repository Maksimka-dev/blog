package com.example.blog.model

import androidx.lifecycle.MutableLiveData
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepository {
    val user: MutableLiveData<User> = mutableLiveData()

    fun getUser() {
        val fUser = FirebaseAuth.getInstance().currentUser
        if (fUser != null) {
            val userRef = FirebaseDatabase.getInstance()
                .getReference("Users/${fUser.uid}")
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.getValue(User::class.java)?.let {
                        user.value = it
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}
