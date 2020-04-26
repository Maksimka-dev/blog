@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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

        model.defaultBitmap = BitmapFactory.decodeResource(this.resources, R.mipmap.tiny)
    }

    //достаем картинку из галереи
    //val clipBtn: Button = activity!!.findViewById(R.id.clipBtn)
    //clipBtn.setOnClickListener {
    //    if(ActivityCompat.checkSelfPermission(activity!!,
    //            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
    //    {
    //        requestPermissions(
    //            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
    //            2000)
    //    }
    //    else {
    //        startGallery()
    //    }
    //}

    //recyclerView.scrollToPosition(model.lastPos)
}

//private fun startGallery() {
//    val cameraIntent =
//        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//    cameraIntent.type = "image/*"
//    if (cameraIntent.resolveActivity(activity!!.packageManager) != null) {
//        startActivityForResult(cameraIntent, 1000)
//    }
//}
//
//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
//{
//    if (resultCode == RESULT_OK) {
//        if (requestCode == 1000) {
//            val returnUri: Uri? = data.data
//            val bitmapImage = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)
//
//            model.bitmapImage = bitmapImage
//        }
//    }
//}
