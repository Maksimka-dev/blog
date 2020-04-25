@file:Suppress("DEPRECATION")

package com.example.blog.main

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.Button
import azadev.android.architecture.core.databinding.inflaters.contentView
import azadev.android.architecture.core.view.MATCH_PARENT
import azadev.android.architecture.core.view.WRAP_CONTENT
import com.example.blog.R
import com.example.blog.blog.BlogActivity
import com.example.blog.databinding.ActivityMainBinding
import com.example.blog.findblog.FindBlogActivity
import com.example.blog.login.LogInActivity
import com.example.blog.newblog.NewBlogActivity
import com.example.blog.signin.SignInActivity

private val PARTS = arrayOf(
    "Blogs" to BlogActivity::class.java,
    "New blog" to NewBlogActivity::class.java,
    "Find blog" to FindBlogActivity::class.java,
    "Log in" to LogInActivity::class.java,
    "Sign in" to SignInActivity::class.java
)

class MainActivity : AppCompatActivity() {

    private val binding by contentView<ActivityMainBinding>(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.parts.removeAllViews()

        PARTS.forEach { part ->
            Button(this).let { button ->
                button.text = part.first
                button.setOnClickListener {
                    startActivity(Intent(this, part.second))
                }

                binding.parts.addView(button, MATCH_PARENT, WRAP_CONTENT)
            }
        }
    }
}
