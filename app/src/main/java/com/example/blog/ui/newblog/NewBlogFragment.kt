@file:Suppress("DEPRECATION")

package com.example.blog.ui.newblog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.blog.R
import com.example.blog.databinding.FragmentNewBlogBinding
import com.example.blog.model.user.User
import com.example.blog.ui.main.MainActivity
import com.example.blog.util.inflaters.contentView
import javax.inject.Inject

class NewBlogFragment : Fragment() {
    private val binding by contentView<FragmentNewBlogBinding>(R.layout.fragment_new_blog)
    private lateinit var navController: NavController

    @Inject
    lateinit var model: NewBlogViewModel

    @Inject
    lateinit var user: MutableLiveData<User?>

    companion object {
        const val SELECT_ICON = "Please select blog icon"
        const val TITLE_TOO_SHORT = "Title is too short"
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity as MainActivity).newBlogComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.model = model
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(view)

        user.observe(viewLifecycleOwner, {
            model.user.value = it
            model.isUserReady = true
        })

        model.createdBlog.observe(viewLifecycleOwner, {
            model.subscribe(it)
        })

        model.subscription.observe(viewLifecycleOwner, {
            navController.navigate(R.id.action_newBlogFragment_to_blogFragment)
        })

        model.validationErrorCommand.observe(this) {
            Toast.makeText(context, TITLE_TOO_SHORT, Toast.LENGTH_SHORT).show()
        }

        model.avatarCommand.observe(this) {
            Toast.makeText(context, SELECT_ICON, Toast.LENGTH_SHORT).show()
        }

        binding.blogPic.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
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
    }

    private fun startGallery() {
        val cameraIntent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        cameraIntent.type = "image/*"
        if (cameraIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(cameraIntent, 1000)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1000) {
                val returnUri: Uri? = data?.data
                val bitmapImage =
                    MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, returnUri)

                binding.blogPic.setImageBitmap(bitmapImage)
                model.bitmapImage = bitmapImage
            }
        }
    }
}
