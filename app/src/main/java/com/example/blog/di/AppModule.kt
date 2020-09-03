package com.example.blog.di

import androidx.lifecycle.MutableLiveData
import com.example.blog.model.user.User
import com.example.blog.util.livedata.mutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun provideMAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    fun provideStorage(): FirebaseStorage = FirebaseStorage.getInstance()

    @Provides
    fun getFirebaseUser(database: FirebaseDatabase, auth: FirebaseAuth): MutableLiveData<User?> {
        val user: MutableLiveData<User?> = mutableLiveData()

        val fUser = auth.currentUser
        if (fUser != null) {
            val userRef = database
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

        return user
    }
}
