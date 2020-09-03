@file:Suppress("DEPRECATION")

package com.example.blog.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.blog.BlogApplication
import com.example.blog.R
import com.example.blog.di.blog.BlogComponent
import com.example.blog.di.chat.ChatComponent
import com.example.blog.di.findblog.FindBlogComponent
import com.example.blog.di.login.LoginComponent
import com.example.blog.di.newblog.NewBlogComponent
import com.example.blog.di.register.RegisterComponent
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    lateinit var blogComponent: BlogComponent
    lateinit var loginComponent: LoginComponent
    lateinit var registerComponent: RegisterComponent
    lateinit var findBlogComponent: FindBlogComponent
    lateinit var newBlogComponent: NewBlogComponent
    lateinit var chatComponent: ChatComponent

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        blogComponent = (application as BlogApplication).appComponent.blogComponent().create()
        blogComponent.inject(this)

        loginComponent = (application as BlogApplication).appComponent.loginComponent().create()
        loginComponent.inject(this)

        registerComponent = (application as BlogApplication).appComponent.registerComponent().create()
        registerComponent.inject(this)

        findBlogComponent = (application as BlogApplication).appComponent.findBlogComponent().create()
        findBlogComponent.inject(this)

        newBlogComponent = (application as BlogApplication).appComponent.newBlogComponent().create()
        newBlogComponent.inject(this)

        chatComponent = (application as BlogApplication).appComponent.chatComponent().create()
        chatComponent.inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_container)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.loginFragment,
                R.id.blogFragment,
                R.id.chatFragment,
                R.id.findBlogFragment,
                R.id.newBlogFragment,
                R.id.registerFragment
            )
        )
        setSupportActionBar(findViewById(R.id.toolbar))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                auth.signOut()
                Navigation.findNavController(findViewById(R.id.nav_host_fragment_container))
                    .navigate(R.id.loginFragment)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
