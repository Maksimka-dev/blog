@file:Suppress("DEPRECATION")

package com.example.blog.ui.chat

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.blog.R
import com.example.blog.databinding.ChatFragmentBinding
import com.example.blog.util.ChatListAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton


class ChatFragment : Fragment(){

    private lateinit var viewModel: ChatViewModel

    companion object{
        fun newInstance()
           = ChatFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ChatFragmentBinding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false)
        binding.viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel = binding.viewModel as ChatViewModel

        viewModel.activity = activity!!
        viewModel.context = context!!
        viewModel.bundle = this.arguments

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.createView()

        val recyclerView: RecyclerView = activity!!.findViewById(R.id.messagesRecyclerView)

        val liveData: MutableLiveData<Boolean> = viewModel.chatLiveData
        liveData.observe(viewLifecycleOwner, Observer {
            if (liveData.value == true) {

                val refreshedAdapter = ChatListAdapter(viewModel.messagesArrayList)
                recyclerView.adapter = refreshedAdapter

                liveData.value = false
            }
        })

        //достаем картинку из галереи
        val uploadImageView: ImageView = activity!!.findViewById(R.id.imageGoogle)
        uploadImageView.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(activity!!,
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

        recyclerView.scrollToPosition(viewModel.lastPos)

        val refreshFab: FloatingActionButton = activity!!.findViewById(R.id.refreshBtn)
        refreshFab.hide()

        val addFab: FloatingActionButton = activity!!.findViewById(R.id.addFab)
        addFab.hide()
    }

    private fun startGallery() {
        val cameraIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(activity!!.packageManager) != null) {
            startActivityForResult(cameraIntent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                val returnUri: Uri? = data.data
                val bitmapImage = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, returnUri)

                val uploadImageView: ImageView = activity!!.findViewById(R.id.imageGoogle)
                uploadImageView.setImageBitmap(bitmapImage)

                viewModel.bitmapImage = bitmapImage
            }
        }
    }
}