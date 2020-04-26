@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R
import com.example.blog.databinding.ChatFragmentBinding
import com.example.blog.inflaters.contentView
import com.example.blog.login.LogInDialogFragment
import com.example.blog.util.ChatListAdapter
import com.example.blog.viewmodel.viewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ChatFragment : Fragment(){

    private val model by viewModel<ChatViewModel>()
    private lateinit var binding: ChatFragmentBinding

    companion object{
        fun newInstance()
           = ChatFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_fragment, null, false)

        binding = ChatFragmentBinding.bind(view)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.model = model
        model.bundle = this.arguments

        val adapter = ChatListAdapter(model)

        model.getMessages()

        model.messageCommand.observe(this){
            model.getTime()
        }

        model.timeCommand.observe(this){
            model.getImages()
        }

        model.imageCommand.observe(this){
            model.setUp()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this.context)
        binding.recyclerView.adapter = adapter
        if (model.messages.size > 0) binding.recyclerView.scrollToPosition(model.messages.size - 1)


        model.items.observe(this, Observer {
            adapter.setData(it.first, it.second, it.third)
        })










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

}