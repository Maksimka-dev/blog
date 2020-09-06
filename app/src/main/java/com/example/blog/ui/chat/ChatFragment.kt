package com.example.blog.ui.chat

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.blog.R
import com.example.blog.databinding.FragmentChatBinding
import com.example.blog.ui.main.MainActivity
import com.example.blog.util.adapters.ChatListAdapter
import com.example.blog.util.extensions.setInvisible
import com.example.blog.util.extensions.setVisible
import com.example.blog.util.inflaters.contentView
import com.example.blog.util.view.NOT_ADMIN
import javax.inject.Inject

class ChatFragment : Fragment() {
    private val binding by contentView<FragmentChatBinding>(R.layout.fragment_chat)
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var adapter: ChatListAdapter

    @Inject
    lateinit var model: ChatViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).chatComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.model = model
        model.blog = args.blog
        model.defaultBitmap = BitmapFactory.decodeResource(resources, R.mipmap.tiny)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.loadChat()

        adapter = ChatListAdapter(model)
        binding.recyclerView.adapter = adapter

        model.chat.observe(viewLifecycleOwner, {
            adapter.addData(it.messages, it.picsUrls, it.times)
            binding.recyclerView.scrollToPosition(it.messages.size - 1)
        })

        model.openGalleryCommand.observe(this) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
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

        model.displayAdminCommand.observe(this) {
            Toast.makeText(context, NOT_ADMIN, Toast.LENGTH_SHORT).show()
        }

        model.hidePreviewCommand.observe(this) {
            hidePreview()
        }
    }

    private fun startGallery() {
        val cameraIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, 1000)
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == 1000) {
                val returnUri: Uri? = data?.data
                val bitmapImage =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, returnUri)

                setPreview(bitmapImage)
                model.bitmapImage = bitmapImage
            }
        }
    }

    private fun setPreview(bitmap: Bitmap) {
        binding.preview.setImageBitmap(bitmap)
        binding.preview.setVisible()
    }

    private fun hidePreview() {
        binding.preview.setInvisible()
        binding.message.setText("")
    }
}
