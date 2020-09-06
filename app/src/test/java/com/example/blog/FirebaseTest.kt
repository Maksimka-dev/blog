package com.example.blog

import com.example.blog.model.blog.Blog
import com.example.blog.model.user.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import junit.framework.Assert.assertTrue
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it

object FirebaseTest : Spek({
    describe("Firebase test") {

        val auth by memoized { FirebaseAuth.getInstance() }
        val database by memoized { FirebaseDatabase.getInstance() }

        val blog by memoized {
            Blog(
                title = "Test",
                ownerId = "Test",
                time = arrayListOf(),
                description = "Test",
                blogId = "Test",
                messages = arrayListOf("test1", "test2")
            )
        }

        val user by memoized {
            User().apply {
                name = "John"
                email = "mail@mail.ru"
                subbs.add(blog.blogId)
            }
        }

        beforeEachTest {
            auth.signInWithEmailAndPassword(user.email, "password")

            database.getReference("Blogs")
                .child(blog.title)
                .setValue(blog)
        }

        it("should be user") {
            assertTrue(auth.currentUser != null)
        }

        it("should be john") {
            assertTrue(auth.currentUser?.email == user.email)
        }

        it("should be test-blog from user's subbs") {
            database.getReference("Blogs").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (blogSnapshot in snapshot.children) {
                        blogSnapshot.getValue(Blog::class.java)?.let {

                            if (user.subbs.contains(it.blogId)) {
                                assertTrue(it.blogId == user.subbs.first())
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }
}
)
