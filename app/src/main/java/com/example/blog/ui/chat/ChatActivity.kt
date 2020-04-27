@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blog.R
import com.example.blog.databinding.ActivityChatBinding
import com.example.blog.inflaters.contentView
import com.example.blog.util.ChatListAdapter
import com.example.blog.viewmodel.viewModel


class ChatActivity : AppCompatActivity(){

    private val model by viewModel<ChatViewModel>()
    private val binding by contentView<ActivityChatBinding>(R.layout.activity_chat)

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.model = model

        val intent = intent
        model.params = Triple(intent.getStringExtra("title"), intent.getStringExtra("blogId"), intent.getStringExtra("ownerId"))

        val adapter = ChatListAdapter(model)

        model.getMessages()

        model.imageCommand.observe(this){
            model.setUp()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        if (model.messages.size > 0) binding.recyclerView.scrollToPosition(model.messages.size - 1)


        model.items.observe(this, Observer {
            adapter.setData(it.first, it.second, it.third)
        })

        model.internetCommand.observe(this){
            val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            model.isInternetAvailable =  activeNetwork?.isConnectedOrConnecting == true
        }

        model.displayInternetCommand.observe(this){
            Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show()
        }

        model.displayAdminCommand.observe(this){
            Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show()
        }

        model.defaultBitmap = BitmapFactory.decodeResource(this.resources, R.mipmap.tiny)

        binding.clipBtn.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    2000)
            }
            else {
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
                val returnUri: Uri? = data!!.data
                val bitmapImage = MediaStore.Images.Media.getBitmap(this.contentResolver, returnUri)

                model.bitmapImage = bitmapImage
            }
        }
    }
}