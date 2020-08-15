@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.example.blog.R
import com.example.blog.databinding.ActivityChatBinding
import com.example.blog.util.adapters.ChatListAdapter
import com.example.blog.util.extensions.isInternetAvailable
import com.example.blog.util.extensions.toast
import com.example.blog.util.inflaters.contentView
import com.example.blog.util.view.NO_INTERNET
import com.example.blog.util.viewmodel.viewModel

class ChatActivity : AppCompatActivity() {
    private val model by viewModel<ChatViewModel>()
    private val binding by contentView<ActivityChatBinding>(
        R.layout.activity_chat
    )

    companion object {
        const val TITLE = "title"
        const val BLOG_ID = "blogId"
        const val OWNER_ID = "ownerId"
        const val NO_ADMIN = "You are not an admin of this blog"
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.model = model

        model.params = Triple(
            intent.getStringExtra(TITLE),
            intent.getStringExtra(BLOG_ID),
            intent.getStringExtra(OWNER_ID)
        )

        val adapter = ChatListAdapter(model)
        binding.recyclerView.adapter = adapter

        model.getMessages()

        model.imageCommand.observe(this) {
            model.setUp()
        }

        if (model.messages.size > 0) binding.recyclerView.scrollToPosition(model.messages.size - 1)

        model.items.observe(this, Observer {
            adapter.setData(it.first, it.second, it.third)
            binding.recyclerView.scrollToPosition(model.messages.size - 1)
        })

        model.internetCommand.observe(this) {
            model.isInternetAvailable = isInternetAvailable(this)
        }

        model.displayInternetCommand.observe(this) {
            toast(NO_INTERNET)
        }

        model.displayAdminCommand.observe(this) {
            toast(NO_ADMIN)
        }

        binding.clipBtn.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    2000
                )
            } else {
                startGallery()
            }
        }

        binding.recyclerView.scrollToPosition(model.messages.size - 1)
    }

    private fun startGallery() {
        val cameraIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(cameraIntent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                val returnUri: Uri? = data?.data
                val bitmapImage = MediaStore.Images.Media.getBitmap(this.contentResolver, returnUri)

                model.bitmapImage = bitmapImage
            }
        }
    }
}
